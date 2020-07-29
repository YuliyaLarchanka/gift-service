package com.epam.esm.web.validator;

import com.epam.esm.web.validator.annotation.ValidOrder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrderValidator implements ConstraintValidator<ValidOrder, String> {
    @Override
    public void initialize(ValidOrder constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null;
    }
}
