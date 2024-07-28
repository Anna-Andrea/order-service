package com.github.anna_andrea.order_service.exception;

/**
 * @author Kevin
 *
 */
public class DistanceServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DistanceServiceException(String message) {
        super(message);
    }

    public DistanceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
