package com.github.anna_andrea.order_service.model.qo;

import javax.validation.constraints.NotNull;

import com.github.anna_andrea.order_service.validation.ValidStatus;

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
public class TakeOrderQo {
	
	/**
	 * order_id
	 */
	private String id;
	
	/**
	 *  "status": "TAKEN"
	 */
	@NotNull(message = "Status must not be null")
	@ValidStatus
	private String status;
}
