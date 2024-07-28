package com.github.anna_andrea.order_service.model.enums;

/**
 * @author Kevin
 *
 */
public enum ExceptionMessages {
	MethodArgumentNotValidException("Invalid request parameters: "),
	ConstraintViolationException("Invalid request parameters: "),
	MissingServletRequestParameterException("Missing request parameter: "),
	MethodArgumentTypeMismatchException("Invalid type for parameter: "),
	TakeOrderException("Take order Failed: "),
	DistanceServiceException("Error fetching distance: "),
	Exception("Internal Server Error: ");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
