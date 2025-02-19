package com.mrunal.taskmanagement.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
		info = @Info(
				contact = @Contact(
						name = "Mrunal",
						email = "mrunalsingade@gmail.com"
				),
				description = "Open API documentation for task management System",
				title = "Task Management System",
				version = "1.0",
				license = @License(
						name = "license name",
						url = "some url"
				),
				termsOfService = "Terms of service"
		),
		servers = {
				@Server(
						description = "Local ENV",
						url = "http://localhost:9292"
				)
		},
		security = {
				@SecurityRequirement(
						name = "bearerAuth"
				)
		}
		)
@SecurityScheme(
		name = "bearerAuth",
		description = "JWT auth token",
		scheme = "bearer",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		in = SecuritySchemeIn.HEADER
		)
public class OpenApiConfig {

}
