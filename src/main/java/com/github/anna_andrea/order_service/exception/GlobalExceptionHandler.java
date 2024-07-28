package com.github.anna_andrea.order_service.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.github.anna_andrea.order_service.model.enums.ExceptionMessages;
import com.github.anna_andrea.order_service.model.enums.TakeOrderExceptionMsg;
import com.github.anna_andrea.order_service.model.vo.ErrorResponse;

/**
 * @author Kevin
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		StringBuilder errorMessage = new StringBuilder(ExceptionMessages.MethodArgumentNotValidException.getMessage());
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
		ErrorResponse errorResponse = new ErrorResponse(
				ExceptionMessages.ConstraintViolationException.getMessage() + ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
		ErrorResponse errorResponse = new ErrorResponse(
				ExceptionMessages.MissingServletRequestParameterException.getMessage() + ex.getParameterName());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		ErrorResponse errorResponse = new ErrorResponse(ExceptionMessages.MethodArgumentTypeMismatchException.getMessage() 
				+ ex.getName() + " - " + ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TakeOrderException.class)
	public ResponseEntity<ErrorResponse> handleTakeOrderException(TakeOrderException ex) {
		ErrorResponse errorResponse = new ErrorResponse(
				ExceptionMessages.TakeOrderException.getMessage() + ex.getMessage());
		if (TakeOrderExceptionMsg.ORDER_ALREADY_TAKEN.getMsg().equals(ex.getMessage())) {
			return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		}
	}

	@ExceptionHandler(DistanceServiceException.class)
	public ResponseEntity<ErrorResponse> handleDistanceServiceException(DistanceServiceException ex) {
		ErrorResponse errorResponse = new ErrorResponse(
				ExceptionMessages.DistanceServiceException.getMessage() + ex.getMessage());
		return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		ErrorResponse errorResponse = new ErrorResponse(ExceptionMessages.Exception.getMessage() + ex.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
