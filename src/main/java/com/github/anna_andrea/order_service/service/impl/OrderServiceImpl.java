package com.github.anna_andrea.order_service.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.github.anna_andrea.order_service.service.OrderService;

/**
 * @author Kevin
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private DistanceService distanceService;

	/**
	 * placeOrder
	 * 
	 * @param placeOrderQo
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public OrderVo placeOrder(PlaceOrderQo placeOrderQo) {
		List<String> origin = placeOrderQo.getOrigin();
		List<String> destination = placeOrderQo.getDestination();
		Order order = new Order();
		order.setId(UUID.randomUUID().toString());
		// The controller layer has performed parameter validation, so the service layer
		// does not repeat the validation.
		int distance = distanceService.getDistanceInMeters(new BigDecimal(origin.get(0)), new BigDecimal(origin.get(1)),
				new BigDecimal(destination.get(0)), new BigDecimal(destination.get(1)));
		order.setDistance(distance);
		order.setStatus(OrderStatus.UNASSIGNED.getStatus());
		order.setStartLatitude(origin.get(0));
		order.setStartLongitude(origin.get(1));
		order.setEndLatitude(destination.get(0));
		order.setEndLongitude(destination.get(1));
		orderMapper.insertOrder(order);
		OrderVo orderVo = new OrderVo();
		BeanUtils.copyProperties(order, orderVo);
		return orderVo;
	}

	/**
	 * Single-node environment: use 'synchronized' for concurrency control.
	 * takeOrder
	 * 
	 * @param takeOrderQo
	 * @return
	 */
	public synchronized TakeOrderVo takeOrder(TakeOrderQo takeOrderQo) {
		checkOrderStatus(takeOrderQo);

		setOrderTaken(takeOrderQo);

		TakeOrderVo takeOrderVo = new TakeOrderVo(TakeOrderStatus.SUCCESS.getStatus());
		return takeOrderVo;
	}

	private void checkOrderStatus(TakeOrderQo takeOrderQo) {
		Order order = orderMapper.selectOrderById(takeOrderQo.getId());
		if (order == null) {
			throw new TakeOrderException(TakeOrderExceptionMsg.ORDER_NOT_FOUND.getMsg());
		}
		if (OrderStatus.TAKEN.getStatus().equals(order.getStatus())) {
			throw new TakeOrderException(TakeOrderExceptionMsg.ORDER_ALREADY_TAKEN.getMsg());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	private void setOrderTaken(TakeOrderQo takeOrderQo) {
		orderMapper.updateOrderStatus(takeOrderQo);
	}

	/**
	 * orderList
	 * 
	 * @param page
	 * @param limit
	 * @return
	 */
	public List<OrderVo> orderList(Integer page, Integer limit) {
		// calculate offset
		Integer offset = (page - 1) * limit;
		OrderListQo orderQo = new OrderListQo(offset, limit);
		return orderMapper.queryOrderList(orderQo);
	}

}
