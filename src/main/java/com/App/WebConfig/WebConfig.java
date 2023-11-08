package com.App.WebConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	// this class will be useful, when you want to display a static webpage. we can return multiple static webpages in below method
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
		// here first parameter["/courses"] means when /courses path is found in URL, return ["courses"] page  
		registry.addViewController("/courses").setViewName("courses.html");
		registry.addViewController("/aboutus").setViewName("about.html");
	}
}
