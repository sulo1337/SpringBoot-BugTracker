package com.ali.bugtracker.customValidators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = TimeFormatValidator.class)
public @interface TimeFormat {

    String message() default "wrong date format";
    Class<?>[] groups() default{};
    Class<? extends Payload> [] payload () default {};
}