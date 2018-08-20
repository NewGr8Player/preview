package com.xavier.preview.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileServiceConfig {

	@Value("${spring.file-service.base-url}")
	public String baseUrl;
	@Value("${spring.file-service.upload-url}")
	public String uploadUrl;
	@Value("${spring.file-service.download-url}")
	public String downloadUrl;
}
