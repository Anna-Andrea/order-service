package com.github.anna_andrea.order_service.model.entity;

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
public class Order {
	/**
	 * id
	 */
	private String id;

	/**
	 * distance
	 */
	private Integer distance;

	/**
	 * status
	 */
	private String status;

	/**
	 * start_latitude
	 */
	private String startLatitude;

	/**
	 * start_longitude
	 */
	private String startLongitude;
	
	/**
	 * end_latitude
	 */
	private String endLatitude;
	
	/**
	 * end_longitude
	 */
	private String endLongitude;
}
