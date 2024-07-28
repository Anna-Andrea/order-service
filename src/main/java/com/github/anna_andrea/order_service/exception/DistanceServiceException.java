package com.github.anna_andrea.order_service.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Kevin
 *
 */
public class DistanceServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final HttpStatus httpStatus;

    public DistanceServiceException(String message) {
        super(message);
        this.httpStatus = null;
    }

    public DistanceServiceException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = null;
    }

    public DistanceServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public DistanceServiceException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
