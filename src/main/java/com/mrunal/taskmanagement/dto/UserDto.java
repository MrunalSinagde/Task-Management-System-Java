package com.mrunal.taskmanagement.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrunal.taskmanagement.entity.Comment;
import com.mrunal.taskmanagement.entity.Role;
import com.mrunal.taskmanagement.entity.Task;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	private int id;
	
	@NotBlank(message = "Username can not be null")
	private String username;
	
	@NotBlank(message = "password can not be null")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	@Email(message = "Please provide a valid email id")
	private String email;
	
	@NotNull(message = "Role can not be null")
	private Role role;
	private List<Task> tasks;
	private List<Comment> comments;
	
}
