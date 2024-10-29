package com.mrunal.taskmanagement.service;

import java.util.Optional;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mrunal.taskmanagement.TaskManagementSystemApplication;
import com.mrunal.taskmanagement.entity.CustomUserDetails;
import com.mrunal.taskmanagement.entity.User;
import com.mrunal.taskmanagement.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{
	
	Logger logger = LogManager.getLogger(TaskManagementSystemApplication.class);
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("inside loadByUsername in CustomerDetailsService");
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if(optionalUser.isEmpty()) {
			throw new UsernameNotFoundException("user does not exist");
		}
	
		return new CustomUserDetails(optionalUser.get());
	}

}
