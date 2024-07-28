package com.github.anna_andrea.order_service.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.anna_andrea.order_service.model.entity.Order;
import com.github.anna_andrea.order_service.model.qo.OrderListQo;
import com.github.anna_andrea.order_service.model.qo.TakeOrderQo;
import com.github.anna_andrea.order_service.model.vo.OrderVo;

/**
 * @author Kevin
 *
 */
@Mapper
public interface OrderMapper {

	/**
	 * insertOrder
	 * @param order
	 */
	void insertOrder(Order order);
	
	/**
	 * selectOrderById
	 * @param id
	 * @return
	 */
	Order selectOrderById(String id);

	/**
	 * updateOrderStatus
	 * @param takeOrderQo
	 * @return
	 */
	int updateOrderStatus(TakeOrderQo takeOrderQo);

	/**
	 * queryOrderList
	 * @param orderListQo
	 * @return
	 */
	List<OrderVo> queryOrderList(OrderListQo orderListQo);
}
