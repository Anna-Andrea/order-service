package com.github.anna_andrea.order_service.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.anna_andrea.order_service.model.vo.ErrorResponse;

/**
 * @author Kevin
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		StringBuilder errorMessage = new StringBuilder("Invalid request parameters: ");
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String defaultMessage = error.getDefaultMessage();
			errorMessage.append(fieldName).append(" - ").append(defaultMessage).append("; ");
		});
		// Remove the trailing semicolon and space
		if (errorMessage.length() > 0) {
			errorMessage.setLength(errorMessage.length() - 2);
		}
		ErrorResponse errorResponse = new ErrorResponse(errorMessage.toString());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
		ErrorResponse errorResponse = new ErrorResponse("Invalid request parameters: " + ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
		ErrorResponse errorResponse = new ErrorResponse("Missing request parameter: " + ex.getParameterName());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleNumberFormatException(NumberFormatException ex) {
    	return new ResponseEntity<>("Invalid number format: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

	@ExceptionHandler(TakeOrderException.class)
	public ResponseEntity<ErrorResponse> handleTakeOrderException(TakeOrderException ex) {
		ErrorResponse errorResponse = new ErrorResponse("Take order Failed: " + ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
    @ExceptionHandler(DistanceServiceException.class)
    public ResponseEntity<ErrorResponse> handleDistanceServiceException(DistanceServiceException ex) {
		ErrorResponse errorResponse = new ErrorResponse("Error fetching distance: "+ ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		ErrorResponse errorResponse = new ErrorResponse("Internal Server Error: " + ex.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
