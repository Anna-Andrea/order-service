package com.github.anna_andrea.order_service.model.qo;

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
public class OrderListQo {
	/**
	 * offset: calculated by page and limit
	 */
	private Integer offset;
	
	private Integer limit;
}
