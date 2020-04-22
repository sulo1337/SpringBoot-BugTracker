package com.ali.bugtracker.customValidators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
public @interface UniqueEmail {

    String message() default "email already exist";
    Class<?>[] groups() default{};
    Class<? extends Payload> [] payload () default {};
}
