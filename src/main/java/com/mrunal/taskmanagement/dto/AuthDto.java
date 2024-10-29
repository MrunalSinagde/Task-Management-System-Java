package com.mrunal.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
	
	@NotBlank(message = "Username can not be null")
	private String username;
	
	@NotBlank(message = "password can not be null")
	private String password;

}
