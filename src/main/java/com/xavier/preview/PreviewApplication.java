package com.xavier.preview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
public class PreviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(PreviewApplication.class, args);
	}

	@Configuration
	@EnableSwagger2
	public class SwaggerConfig {
		@Bean
		public Docket api() {
			return new Docket(DocumentationType.SWAGGER_2)
					.useDefaultResponseMessages(false)
					.select()
					.apis(RequestHandlerSelectors.basePackage("com.xavier.preview.api"))
					.build()
					.apiInfo(apiInfo());
		}

		private ApiInfo apiInfo() {
			return new ApiInfo(
					"Office converter API",
					"Automates conversions between office document formats using LibreOffice or Apache OpenOffice.",
					"0.1",
					"Terms of service",
					new Contact("NewGr8Player", "NewGr8player.gitee.io", "xavier@programmer.net"),
					"MIT LICENCE",
					"https://github.com/NewGr8Player/preview/blob/master/LICENSE",
					Collections.emptyList());
		}
	}
}
