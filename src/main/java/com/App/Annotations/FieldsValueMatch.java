package com.App.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.App.CustomValidationsLogic.FieldsValueMatchValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// The purpose of this annotation is to confirm 2 fields are matching
// like email & confirm email, password & confirm password

@Constraint(validatedBy = FieldsValueMatchValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsValueMatch {
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
	String message() default "Fields values don't match!";

    String field();

    String fieldMatch();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldsValueMatch[] value();
    }
}
