package com.App.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/*
@ControllerAdvice is a specialization of the @Component annotation which allows to handle
exceptions across the whole application in one global handling component. It can be viewed
as an interceptor of exceptions thrown by methods annotated with @RequestMapping and similar.
Watch notes if a bit confused
* */

@Slf4j
@ControllerAdvice(annotations = Controller.class) // means this logic to be executed only if exceptions occuring in
													// classes with @Controller annotations
public class GlobalExceptionHandler {

	/*
	 * @ExceptionHandler will register the given method for a given exception type,
	 * so that ControllerAdvice can invoke this method logic if a given exception
	 * type is thrown inside the web application.
	 */

	// Exception class will take care of all the exceptions in the application
	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception e) {
		String errorMsg = null;
		ModelAndView errorPage = new ModelAndView();
		errorPage.setViewName("error"); // provide the html page that you wanna show post exception
		if (e.getMessage() != null) {
			errorMsg = e.getMessage();
		} else if (e.getCause() != null) {
			errorMsg = e.getCause().toString();
		} else if (e != null) {
			errorMsg = e.toString();
		}
		errorPage.addObject("errorMsg", errorMsg);
		return errorPage;
	}
}
