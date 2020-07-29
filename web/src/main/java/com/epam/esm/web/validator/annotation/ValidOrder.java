package com.epam.esm.web.validator.annotation;

import com.epam.esm.web.validator.OrderValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrderValidator.class)
public @interface ValidOrder {
//    String message() default "Ban reason id is not valid";
//
//    Class<?>[] groups() default {};
//
//    Class<? extends Payload>[] payload() default {};
}
