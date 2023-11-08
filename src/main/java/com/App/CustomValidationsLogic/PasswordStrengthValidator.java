package com.App.CustomValidationsLogic;

import java.util.Arrays;
import java.util.List;

import com.App.Annotations.PasswordValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordValidator, String> {

	List<String> weakPasswords; // maintaining a weak passwords list

	@Override
	public void initialize(PasswordValidator passwordValidator) {
		weakPasswords = Arrays.asList("12345", "password", "qwerty");
	}

	@Override
	public boolean isValid(String passwordField, ConstraintValidatorContext cxt) {
		return passwordField != null && (!weakPasswords.contains(passwordField)); // here if both conditions are true,
																					// boolean value that will be
																					// returned will be true, and if
																					// either one one of them fails, the
																					// boolean value returned will be
																					// false i.e the password is not
																					// valid
	}

}
