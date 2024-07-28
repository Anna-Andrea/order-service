package com.github.anna_andrea.order_service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;

import com.github.anna_andrea.order_service.model.enums.OrderStatus;
import com.github.anna_andrea.order_service.model.qo.PlaceOrderQo;
import com.github.anna_andrea.order_service.model.qo.TakeOrderQo;
import com.github.anna_andrea.order_service.model.vo.OrderVo;
import com.github.anna_andrea.order_service.model.vo.TakeOrderVo;

/**
 * IntegrationTest
 * 
 * testOrderFlow: testEmptyOrderList -> testPlaceOrder -> testTakeOrder(concurrent) -> testNonEmptyOrderList
 * 
 * @author Kevin
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerIntegrationTest {
	
    private static final Logger logger = LoggerFactory.getLogger(OrderControllerIntegrationTest.class);
	
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    @Order(1)
    public void testEmptyOrderList() {
        // 1. testEmptyOrderList
		logger.info("(1) Start Test Empty Order List:");
		String orderListUrl = "/orders?page=1&limit=10";
		ResponseEntity<List<OrderVo>> orderListResponse = restTemplate.exchange(orderListUrl, HttpMethod.GET, null, 
				new ParameterizedTypeReference<List<OrderVo>>() {});
		assertThat(orderListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		List<OrderVo> orderList = orderListResponse.getBody();
	    assertThat(orderList).isNotNull();
	    assertThat(orderList.size()).isEqualTo(0);
    }

	@Test
	@Order(2)
	public void testPlaceOrderAndTakeOrder() throws InterruptedException{
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		
		// 2. testPlaceOrder
		logger.info("(2) Start Test Place Order:");
		PlaceOrderQo placeOrderQo = new PlaceOrderQo(Arrays.asList("37.7749", "-122.4194"),
				Arrays.asList("34.0522", "-118.2437"));
		HttpEntity<PlaceOrderQo> placeOrderRequest = new HttpEntity<>(placeOrderQo);
		ResponseEntity<OrderVo> placeOrderResponse = restTemplate.exchange("/orders", HttpMethod.POST, placeOrderRequest,
				OrderVo.class);
		assertThat(placeOrderResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		OrderVo orderVo = placeOrderResponse.getBody();
		assertThat(orderVo).isNotNull();
		String orderId = orderVo.getId();

	    // 3. testTakeOrder with Concurrent Requests
		logger.info("(3) Start Test Take Order with Concurrent Requests:");
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        TakeOrderQo takeOrderQo = new TakeOrderQo();
        takeOrderQo.setStatus(OrderStatus.TAKEN.getStatus());

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                	
                    HttpEntity<TakeOrderQo> takeOrderRequest = new HttpEntity<>(takeOrderQo);
                    ResponseEntity<TakeOrderVo> takeOrderResponse = restTemplate.exchange("/orders/" + orderId,
                            HttpMethod.PATCH, takeOrderRequest, TakeOrderVo.class);
                    if (takeOrderResponse.getStatusCode() == HttpStatus.OK) {
                        successCount.incrementAndGet();
                    } else if (takeOrderResponse.getStatusCode() == HttpStatus.CONFLICT){
                    	// ORDER_ALREADY_TAKEN
                    	failCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // Ensure only one request succeeded
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(threadCount - 1);
	}

	@Test
	public void testNonEmptyOrderList(){
        // 4.testNonEmptyOrderList
		logger.info("(4) Start Test Non-Empty Order List:");
		String orderListUrl = "/orders?page=1&limit=10";
		ResponseEntity<List<OrderVo>> orderListResponse = restTemplate.exchange(orderListUrl, HttpMethod.GET, null, 
				new ParameterizedTypeReference<List<OrderVo>>() {});
		assertThat(orderListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		List<OrderVo> orderList = orderListResponse.getBody();
		assertThat(orderList).isNotNull();
		assertThat(orderList).isNotEmpty();
	}
	
}
