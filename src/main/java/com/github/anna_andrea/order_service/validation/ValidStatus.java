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
@Constraint(validatedBy = StatusValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStatus {

    String message() default "Status must be 'TAKEN'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
