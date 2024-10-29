package com.mrunal.taskmanagement.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrunal.taskmanagement.TaskManagementSystemApplication;
import com.mrunal.taskmanagement.dto.AuthDto;
import com.mrunal.taskmanagement.dto.TaskDto;
import com.mrunal.taskmanagement.dto.UserDto;
import com.mrunal.taskmanagement.exception.NotAuthorisedException;
import com.mrunal.taskmanagement.exception.TaskNotFoundException;
import com.mrunal.taskmanagement.exception.UserNotFoundException;
import com.mrunal.taskmanagement.service.JwtService;
import com.mrunal.taskmanagement.service.TaskService;
import com.mrunal.taskmanagement.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
	
	Logger logger = LogManager.getLogger(TaskManagementSystemApplication.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserDto>> getAllUsers() throws UserNotFoundException {
		logger.info("Inside getAllUsers of UserController");
		return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
	}
	
	@GetMapping("/getUser/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable int id, Authentication authentication) throws UserNotFoundException, NotAuthorisedException {
		logger.info("Inside getUserById of UserController");
		return new ResponseEntity<>(userService.getUserById(id,authentication),HttpStatus.OK);
	}
	
	@PostMapping(value = "/setUser", consumes = {"application/json"} )
	public  ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) throws NotAuthorisedException {
		logger.info("Inside createUser of UserController");
		return new ResponseEntity<>(userService.createUser(userDto),HttpStatus.OK);
	}
	
	@PutMapping(value = "/updateUser/{id}", consumes = {"application/json"})
	public ResponseEntity<UserDto> updateUser(@PathVariable int id, @RequestBody UserDto userDto,Authentication authentication) throws UserNotFoundException, NotAuthorisedException {
		logger.info("Inside updateUser of UserController");
		return new ResponseEntity<>(userService.updateUserById(userDto,id,authentication),HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/deleteUser/{id}")
	public ResponseEntity<String> deleteUserById(@PathVariable int id, Authentication authentication) throws NotAuthorisedException, UserNotFoundException {
		String result = userService.deleteUserById(id,authentication);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public String authenticateAndGetToken(@RequestBody @Valid AuthDto authDto) {
		logger.info("Inside authenticateAndGetToken of UserController");
		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword()));
		if(authentication.isAuthenticated()) {
			return jwtService.generateToken(authDto.getUsername());
		}
		return "Invalid user";
	}
	
	@GetMapping("/getMyTasks")
	public ResponseEntity<List<TaskDto>> getTasksByUserId(Authentication authentication) throws TaskNotFoundException{
		logger.info("Inside getTasksByUserId of UserController");
		
		return new ResponseEntity<>(taskService.getTasksByUserId(authentication),HttpStatus.OK);
	}

}
