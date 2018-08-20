package com.xavier.preview.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JodConverterConfig {

	@Value("${spring.converter.office-home}")
	public String officePath;
}
