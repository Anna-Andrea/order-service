package com.github.anna_andrea.order_service.exception;

/**
 * @author Kevin
 *
 */
public class TakeOrderException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TakeOrderException(String message) {
		super(message);
	}
}
