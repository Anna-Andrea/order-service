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
public class ErrorResponse {
	/**
	 * ERROR: ERROR_DESCRIPTION
	 */
	private String error;
}
