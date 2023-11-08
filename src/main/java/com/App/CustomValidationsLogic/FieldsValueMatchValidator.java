package com.App.CustomValidationsLogic;

import org.springframework.beans.BeanWrapperImpl;

import com.App.Annotations.FieldsValueMatch;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {

	private String field;
	private String fieldMatch;

	@Override
	public void initialize(FieldsValueMatch constraintAnnotation) {
		this.field = constraintAnnotation.field();
		this.fieldMatch = constraintAnnotation.fieldMatch();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field); // getting just the field value from the
																				// entire object that is passed with the
																				// name 'value'
		Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch); // similarly getting field
																							// match value from the
																							// object. Like
																							// 'confirmPassword'.
		
		
		if (fieldValue != null) {
			if (fieldValue.toString().startsWith("$2a")) {
				return true;
			} else {
				return fieldValue.equals(fieldMatchValue);
			}
		} else {
			return fieldMatchValue == null;
		}
		
		
		/*
        if (fieldValue != null) {
            return fieldValue.equals(fieldMatchValue);
        } else {
            return fieldMatchValue == null;
        }
        */

	}

}
