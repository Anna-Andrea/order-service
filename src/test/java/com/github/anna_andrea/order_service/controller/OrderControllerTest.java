package com.github.anna_andrea.order_service.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.anna_andrea.order_service.model.enums.OrderStatus;
import com.github.anna_andrea.order_service.model.qo.PlaceOrderQo;
import com.github.anna_andrea.order_service.model.qo.TakeOrderQo;
import com.github.anna_andrea.order_service.model.vo.OrderVo;
import com.github.anna_andrea.order_service.model.vo.TakeOrderVo;
import com.github.anna_andrea.order_service.service.OrderService;

/**
 * @author Kevin
 *
 */
@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;
    
    private PlaceOrderQo validPlaceOrderQo;
    private TakeOrderQo validTakeOrderQo;
    private List<OrderVo> orderList;

    @BeforeEach
    public void setup() {
        orderList = Arrays.asList(new OrderVo(), new OrderVo());
        validPlaceOrderQo = new PlaceOrderQo(
                Arrays.asList("37.7749", "-122.4194"),
                Arrays.asList("34.0522", "-118.2437")
        );
        validTakeOrderQo = new TakeOrderQo();
        validTakeOrderQo.setStatus(OrderStatus.TAKEN.getStatus());

        Mockito.when(orderService.orderList(Mockito.anyInt(), Mockito.anyInt())).thenReturn(orderList);
        Mockito.when(orderService.placeOrder(Mockito.any(PlaceOrderQo.class))).thenReturn(new OrderVo());
        Mockito.when(orderService.takeOrder(Mockito.any(TakeOrderQo.class))).thenReturn(new TakeOrderVo());
    }
    
    @Test
    public void testOrderList_ValidRequest() throws Exception {
        mockMvc.perform(get("/orders")
                		.param("page", "1")
                		.param("limit", "10"))
        		.andExpect(status().isOk())
        		.andExpect(content().json(objectMapper.writeValueAsString(orderList)));
        
        // Test very large page and limit values
        mockMvc.perform(get("/orders")
                		.param("page", String.valueOf(Integer.MAX_VALUE))
                		.param("limit", "1000"))
        		.andExpect(status().isOk())
        		.andExpect(content().json(objectMapper.writeValueAsString(orderList)));
    }
    
    @Test
    public void testOrderList_InvalidParams() throws Exception {
    	 // 1. Test Missing Parameter (MissingServletRequestParameterException)
        mockMvc.perform(get("/orders")
                        .param("limit", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> 
                	assertTrue(result.getResolvedException() instanceof MissingServletRequestParameterException));

        mockMvc.perform(get("/orders")
                        .param("page", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> 
                    assertTrue(result.getResolvedException() instanceof MissingServletRequestParameterException));

        // 2. Test Invalid Non-integer Values (NumberFormatException)
//        mockMvc.perform(get("/orders")
//                        .param("page", "abc")
//                        .param("limit", "1"))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> 
//                    assertTrue(result.getResolvedException() instanceof NumberFormatException));
//
//        mockMvc.perform(get("/orders")
//                        .param("page", "1")
//                        .param("limit", "xyz"))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> 
//                    assertTrue(result.getResolvedException() instanceof NumberFormatException));

        // 3. Test Invalid Integer Values (ConstraintViolationException)
        mockMvc.perform(get("/orders")
                        .param("page", "0")
                        .param("limit", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> 
                    assertTrue(result.getResolvedException() instanceof ConstraintViolationException));

        mockMvc.perform(get("/orders")
                        .param("page", "1")
                        .param("limit", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> 
                    assertTrue(result.getResolvedException() instanceof ConstraintViolationException));

        mockMvc.perform(get("/orders")
                        .param("page", "-1")
                        .param("limit", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> 
                    assertTrue(result.getResolvedException() instanceof ConstraintViolationException));

        mockMvc.perform(get("/orders")
                        .param("page", "1")
                        .param("limit", "-10"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> 
                    assertTrue(result.getResolvedException() instanceof ConstraintViolationException));  
    }
    
    @Test
    public void testPlaceOrder_ValidRequest() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPlaceOrderQo)))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testPlaceOrder_InvalidParams() throws Exception {
        // 1. Test @NotNull and @Size violations (MethodArgumentNotValidException)
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"origin\":null, \"destination\": [\"34.0522\", \"-118.2437\"]}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"origin\":[], \"destination\": [\"34.0522\", \"-118.2437\"]}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"origin\": [\"34.0522\"], \"destination\": [\"34.0522\", \"-118.2437\"]}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"origin\": [\"34.0522\", \"-118.2437\", \"extra\"], \"destination\": [\"34.0522\", \"-118.2437\"]}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"origin\": [\"34.0522\", \"-118.2437\"], \"destination\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"origin\": [\"34.0522\", \"-118.2437\"], \"destination\": []}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        // 2. Test @ValidLatitudeLongitude violations (MethodArgumentNotValidException)
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"origin\": [\"invalid_lat\", \"invalid_lng\"], \"destination\": [\"34.0522\", \"-118.2437\"]}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"origin\": [\"91\", \"-118.2437\"], \"destination\": [\"34.0522\", \"-118.2437\"]}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"origin\": [\"34.0522\", \"-200\"], \"destination\": [\"34.0522\", \"-118.2437\"]}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    
    @Test
    public void testTakeOrder_ValidRequest() throws Exception {
        mockMvc.perform(patch("/orders/{id}", "test-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTakeOrderQo)))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testTakeOrder_InvalidParams() throws Exception {
        // 1. Test @NotNull violation with status as null (MethodArgumentNotValidException)
        mockMvc.perform(patch("/orders/{id}", "test-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        // 2. Test @ValidStatus violation with invalid status values (MethodArgumentNotValidException)
        mockMvc.perform(patch("/orders/{id}", "test-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"UNASSIGNED\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        mockMvc.perform(patch("/orders/{id}", "test-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"COMPLETED\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        mockMvc.perform(patch("/orders/{id}", "test-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"INVALID_STATUS\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

}
