package com.github.anna_andrea.order_service.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kevin
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo {
	/**
	 * order_id
	 */
	private String id;

	/**
	 * total_distance
	 */
	private Integer distance;

	/**
	 * ORDER_STATUS
	 */
	private String status;
}
