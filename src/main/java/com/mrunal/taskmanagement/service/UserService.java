package com.mrunal.taskmanagement.service;

import java.util.List;


import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrunal.taskmanagement.TaskManagementSystemApplication;
import com.mrunal.taskmanagement.dto.UserDto;
import com.mrunal.taskmanagement.entity.CustomUserDetails;
import com.mrunal.taskmanagement.entity.Role;
import com.mrunal.taskmanagement.entity.User;
import com.mrunal.taskmanagement.exception.NotAuthorisedException;
import com.mrunal.taskmanagement.exception.UserNotFoundException;
import com.mrunal.taskmanagement.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	Logger logger = LogManager.getLogger(TaskManagementSystemApplication.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Transactional
	public UserDto createUser(UserDto userDto) throws NotAuthorisedException {
		logger.info("Inside createUser of UserService");

		if (userDto.getRole().equals(Role.ROLE_ADMIN)) {
			throw new NotAuthorisedException("You can not create an admin, you can contact the "
					+ "developer team if you think this is a mistake");
		}

		System.out.println(userDto.getPassword());

		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		User user = objectMapper.convertValue(userDto, User.class);
		user.setPassword(userDto.getPassword());

		User savedUser = userRepository.save(user);
		return objectMapper.convertValue(savedUser, UserDto.class);
	}

	public List<UserDto> getAllUsers() throws UserNotFoundException {
		logger.info("Inside getAllUsers of UserController");

		List<User> users = userRepository.findAll();
		TypeReference<List<UserDto>> typeReference = new TypeReference<List<UserDto>>() {
		};
		List<UserDto> userDtoList = objectMapper.convertValue(users, typeReference);
		
		if(userDtoList.isEmpty()) {
			throw new UserNotFoundException("There are no users in the database currently");
		}
		return userDtoList;
	}

	public UserDto getUserById(int id, Authentication authentication)
			throws UserNotFoundException, NotAuthorisedException {
		logger.info("Inside getUserById of UserService");

		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isEmpty()) {
			throw new UserNotFoundException("User not found with id: " + id);
		}

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		if (userDetails.getUser().getId() != id && (userDetails.getUser().getRole().equals(Role.ROLE_USER))) {
			throw new NotAuthorisedException("You are not authorised to access this user");
		}
		return objectMapper.convertValue(optionalUser.get(), UserDto.class);
	}

	@Transactional
	public UserDto updateUserById(UserDto updatedUser, int id, Authentication authentication)
			throws UserNotFoundException, NotAuthorisedException {
		logger.info("Inside updateUserById of UserService");

		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isEmpty()) {
			throw new UserNotFoundException("User not found with id: " + id);
		}

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		if (userDetails.getUser().getId() != id && (userDetails.getUser().getRole().equals(Role.ROLE_USER))) {
			throw new NotAuthorisedException("You are not authenticated to update this user's credentials");
		}

		User existingUser = optionalUser.get();
		if (updatedUser.getUsername() != null)
			existingUser.setUsername(updatedUser.getUsername());

		if (updatedUser.getRole() != null)
			existingUser.setRole(updatedUser.getRole());

		if (updatedUser.getEmail() != null)
			existingUser.setEmail(updatedUser.getEmail());

		if (updatedUser.getTasks() != null)
			existingUser.setTasks(updatedUser.getTasks());

		if (updatedUser.getComments() != null)
			existingUser.setComments(updatedUser.getComments());

		if (updatedUser.getPassword() != null)
			existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

		User savedUser = userRepository.save(existingUser);
		return objectMapper.convertValue(savedUser, UserDto.class);
	}

	@Transactional
	public String deleteUserById(int id, Authentication authentication) throws NotAuthorisedException, UserNotFoundException {
		logger.info("Inside deleteUserById of UserService");
		
		Optional<User> optionalUser = userRepository.findById(id);
		if(optionalUser.isEmpty()) {
			throw new UserNotFoundException("User not found with id: " +id);
		}

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		if (userDetails.getUser().getId() != id && (userDetails.getUser().getRole().equals(Role.ROLE_USER))) {
			throw new NotAuthorisedException("You are not authenticated to delete this user");
		}
		userRepository.deleteById(id);
		return "User with id " + id + " is deleted";
	}

}
