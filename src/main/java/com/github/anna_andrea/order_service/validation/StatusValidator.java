package com.github.anna_andrea.order_service.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.github.anna_andrea.order_service.model.enums.OrderStatus;

/**
 * @author Kevin
 *
 */
public class StatusValidator implements ConstraintValidator<ValidStatus, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return OrderStatus.TAKEN.getStatus().equals(value);
    }
}