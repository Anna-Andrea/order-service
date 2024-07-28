package com.github.anna_andrea.order_service.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author Kevin
 *
 */
@Constraint(validatedBy = LatitudeLongitudeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLatitudeLongitude {
    String message() default "Invalid latitude or longitude";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
