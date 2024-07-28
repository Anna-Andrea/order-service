package com.github.anna_andrea.order_service.service;

import java.util.List;

import com.github.anna_andrea.order_service.model.qo.PlaceOrderQo;
import com.github.anna_andrea.order_service.model.qo.TakeOrderQo;
import com.github.anna_andrea.order_service.model.vo.OrderVo;
import com.github.anna_andrea.order_service.model.vo.TakeOrderVo;

/**
 * @author Kevin
 *
 */
public interface OrderService {

	/**
	 * placeOrder
	 * 
	 * @param placeOrderQo
	 * @return
	 */
	OrderVo placeOrder(PlaceOrderQo placeOrderQo);

	/**
	 * takeOrder
	 * 
	 * @param takeOrderQo
	 * @return
	 */
	TakeOrderVo takeOrder(TakeOrderQo takeOrderQo);

	/**
	 * orderList
	 * 
	 * @param page
	 * @param limit
	 * @return
	 */
	List<OrderVo> orderList(Integer page, Integer limit);
}
