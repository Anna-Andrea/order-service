package com.github.anna_andrea.order_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.anna_andrea.order_service.exception.TakeOrderException;
import com.github.anna_andrea.order_service.mapper.OrderMapper;
import com.github.anna_andrea.order_service.model.entity.Order;
import com.github.anna_andrea.order_service.model.enums.OrderStatus;
import com.github.anna_andrea.order_service.model.enums.TakeOrderExceptionMsg;
import com.github.anna_andrea.order_service.model.enums.TakeOrderStatus;
import com.github.anna_andrea.order_service.model.qo.OrderListQo;
import com.github.anna_andrea.order_service.model.qo.PlaceOrderQo;
import com.github.anna_andrea.order_service.model.qo.TakeOrderQo;
import com.github.anna_andrea.order_service.model.vo.OrderVo;
import com.github.anna_andrea.order_service.model.vo.TakeOrderVo;
import com.github.anna_andrea.order_service.service.impl.DistanceService;
import com.github.anna_andrea.order_service.service.impl.OrderServiceImpl;

public class OrderServiceTest {
	@Mock
	private OrderMapper orderMapper;

	@Mock
	private DistanceService distanceService;

	@InjectMocks
	@Autowired
	private OrderServiceImpl orderService;
	
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

	@Test
	public void testPlaceOrder_ValidService() {
		// prepare test data
		PlaceOrderQo placeOrderQo = new PlaceOrderQo();
		placeOrderQo.setOrigin(Arrays.asList("40.712776", "-74.005974"));
		placeOrderQo.setDestination(Arrays.asList("34.052235", "-118.243683"));

		when(distanceService.getDistanceInMeters(any(), any(), any(), any())).thenReturn(1000);

		OrderVo result = orderService.placeOrder(placeOrderQo);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderMapper).insertOrder(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        
		verify(distanceService).getDistanceInMeters(any(), any(), any(), any());
		
		assertNotNull(result);
		assertEquals(capturedOrder.getId(), result.getId());
		assertEquals(OrderStatus.UNASSIGNED.getStatus(),result.getStatus());
		assertEquals(1000, result.getDistance());
	}
	
    @Test
    public void testOrderList_NormalCase() {
        // Prepare test data
        Integer page = 2;
        Integer limit = 5;
        Integer offset = (page - 1) * limit;

        OrderListQo orderQo = new OrderListQo(offset, limit);

        OrderVo order1 = new OrderVo();
        OrderVo order2 = new OrderVo();
        List<OrderVo> orderList = Arrays.asList(order1, order2);

        when(orderMapper.queryOrderList(orderQo)).thenReturn(orderList);

        // Call the method
        List<OrderVo> result = orderService.orderList(page, limit);

        // Verify results
        assertEquals(2, result.size());
        assertEquals(orderList, result);
        verify(orderMapper).queryOrderList(orderQo);
    }
    
    @Test
    public void testOrderList_EmptyResult() {
        // Prepare test data
        Integer page = 1;
        Integer limit = 10;
        Integer offset = (page - 1) * limit;

        OrderListQo orderQo = new OrderListQo(offset, limit);

        when(orderMapper.queryOrderList(orderQo)).thenReturn(Arrays.asList());

        // Call the method
        List<OrderVo> result = orderService.orderList(page, limit);

        // Verify results
        assertEquals(0, result.size());
        verify(orderMapper).queryOrderList(orderQo);
    }
    
    @Test
    public void testOrderList_BoundaryConditions() {
        // Test with minimum values
        Integer page = 1;
        Integer limit = 1;
        Integer offset = (page - 1) * limit;

        OrderListQo orderQo = new OrderListQo(offset, limit);

        OrderVo order = new OrderVo();
        List<OrderVo> orderList = Arrays.asList(order);

        when(orderMapper.queryOrderList(orderQo)).thenReturn(orderList);

        // Call the method
        List<OrderVo> result = orderService.orderList(page, limit);

        // Verify results
        assertEquals(1, result.size());
        assertEquals(orderList, result);
        verify(orderMapper).queryOrderList(orderQo);

        // Test with larger values if applicable
        page = 100;
        limit = 50;
        offset = (page - 1) * limit;

        orderQo = new OrderListQo(offset, limit);

        when(orderMapper.queryOrderList(orderQo)).thenReturn(orderList);

        // Call the method
        result = orderService.orderList(page, limit);

        // Verify results
        assertEquals(1, result.size());
        assertEquals(orderList, result);
        verify(orderMapper).queryOrderList(orderQo);
    }
    
    @Test
    public void testTakeOrder_ConcurrentRequests() throws InterruptedException {
        // Prepare test data
        TakeOrderQo takeOrderQo = new TakeOrderQo();
        takeOrderQo.setId("test-id");
        takeOrderQo.setStatus(OrderStatus.TAKEN.getStatus());

        Order unassignedOrder = new Order();
        unassignedOrder.setId("test-id");
        unassignedOrder.setStatus(OrderStatus.UNASSIGNED.getStatus());

        Order takenOrder = new Order();
        takenOrder.setId("test-id");
        takenOrder.setStatus(OrderStatus.TAKEN.getStatus());

        // Set up stubbing to simulate the change of state
        when(orderMapper.selectOrderById(takeOrderQo.getId()))
            .thenReturn(unassignedOrder) // First call returns UNASSIGNED
            .thenReturn(takenOrder); // Subsequent calls return TAKEN

        when(orderMapper.updateOrderStatus(any(TakeOrderQo.class))).thenReturn(1);

        // Set up concurrent requests
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    TakeOrderVo result = orderService.takeOrder(takeOrderQo);
                    assertEquals(TakeOrderStatus.SUCCESS.getStatus(), result.getStatus());
                } catch (TakeOrderException e) {
                    // Check if the exception is due to the order already being taken
                    assertEquals(TakeOrderExceptionMsg.ORDER_ALREADY_TAKEN.getMsg(), e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // Verify selectOrderById is called multiple times, but updateOrderStatus is called only once
        verify(orderMapper, times(threadCount)).selectOrderById(takeOrderQo.getId());
        verify(orderMapper, times(1)).updateOrderStatus(takeOrderQo);
    }
    
}
