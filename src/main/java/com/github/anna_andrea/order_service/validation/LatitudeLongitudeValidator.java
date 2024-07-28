package com.github.anna_andrea.order_service.validation;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Kevin
 *
 */
public class LatitudeLongitudeValidator implements ConstraintValidator<ValidLatitudeLongitude, List<String>> {

    private static final BigDecimal MIN_LATITUDE = new BigDecimal("-90.0");
    private static final BigDecimal MAX_LATITUDE = new BigDecimal("90.0");
    private static final BigDecimal MIN_LONGITUDE = new BigDecimal("-180.0");
    private static final BigDecimal MAX_LONGITUDE = new BigDecimal("180.0");

    @Override
    public void initialize(ValidLatitudeLongitude constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        if (values == null || values.size() != 2) {
            return false;
        }
        try {
            BigDecimal latitude = new BigDecimal(values.get(0));
            BigDecimal longitude = new BigDecimal(values.get(1));
            
            return latitude.compareTo(MIN_LATITUDE) >= 0 &&
                   latitude.compareTo(MAX_LATITUDE) <= 0 &&
                   longitude.compareTo(MIN_LONGITUDE) >= 0 &&
                   longitude.compareTo(MAX_LONGITUDE) <= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

