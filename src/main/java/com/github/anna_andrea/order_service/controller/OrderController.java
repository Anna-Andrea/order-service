package com.github.anna_andrea.order_service.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.anna_andrea.order_service.model.qo.PlaceOrderQo;
import com.github.anna_andrea.order_service.model.qo.TakeOrderQo;
import com.github.anna_andrea.order_service.model.vo.OrderVo;
import com.github.anna_andrea.order_service.model.vo.TakeOrderVo;
import com.github.anna_andrea.order_service.service.OrderService;

/**
 * @author Kevin
 *
 */
@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {
	@Autowired
	private OrderService orderService;

	/**
	 * Place order
	 * 
	 * @param placeOrderQo
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> placeOrder(@RequestBody @Valid PlaceOrderQo placeOrderQo) {
		OrderVo orderVo = orderService.placeOrder(placeOrderQo);
		return new ResponseEntity<OrderVo>(orderVo, HttpStatus.OK);
	}

	/**
	 * takeOrder
	 * 
	 * @param id
	 * @param takeOrderQo
	 * @return
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<?> takeOrder(@PathVariable("id") String id, @RequestBody @Valid TakeOrderQo takeOrderQo) {
		TakeOrderVo takeOrderVo = orderService.takeOrder(takeOrderQo);
		return new ResponseEntity<TakeOrderVo>(takeOrderVo, HttpStatus.OK);
	}

	/**
	 * Order list
	 * 
	 * @param page
	 * @param limit
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> orderList(
			@RequestParam @NotNull(message = "Page must not be null") @Min(value = 1, message = "Page number must start with 1") Integer page,
			@RequestParam @NotNull(message = "Limit must not be null") @Min(value = 1, message = "Limit must be a positive integer") Integer limit) {
		List<OrderVo> orderList = orderService.orderList(page, limit);
		return new ResponseEntity<List<OrderVo>>(orderList, HttpStatus.OK);
	}
}
