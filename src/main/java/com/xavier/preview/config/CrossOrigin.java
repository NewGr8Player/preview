package com.xavier.preview.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 处理跨域问题
 *
 * @author NewGr8Player
 */
@Configuration
public class CrossOrigin implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				/*POST, GET, PUT, PATCH, DELETE, OPTIONS*/
				.allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
				.allowCredentials(true).maxAge(3600);
	}
}
