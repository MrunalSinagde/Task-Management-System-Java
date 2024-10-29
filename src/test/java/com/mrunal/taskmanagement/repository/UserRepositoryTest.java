package com.mrunal.taskmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mrunal.taskmanagement.entity.Role;
import com.mrunal.taskmanagement.entity.User;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	@Order(1)
	public void createUser() {
		User user = new User();
		user.setUsername("test user");
		user.setEmail("testemail@gmail.com");
		user.setPassword("test passsword");
		user.setRole(Role.ROLE_USER);
		
		User savedUser = userRepository.save(user);
		
		assertNotNull(userRepository.findById(savedUser.getId()));
	}
	
	@Test
	@Order(2)
	public void testReadAll() {
		List<User> users = userRepository.findAll();
		
		assertThat(users).size().isGreaterThan(0);
	}
	
	@Test
	@Order(3)
	public void testRead() {
		System.out.println(userRepository.maxUserId());
		User user = userRepository.findById(userRepository.maxUserId()).get();
		assertEquals("test user", user.getUsername());
	}
	
	@Test
	@Order(4)
	public void testUpdate() {
		User user = userRepository.findById(userRepository.maxUserId()).get();
		user.setRole(Role.ROLE_ADMIN);
		userRepository.save(user);
		
		assertNotEquals(Role.ROLE_USER, userRepository.findById(userRepository.maxUserId()).get().getRole());
	}
	
	@Test
	@Order(5)
	public void testDelete() {
		int id = userRepository.maxUserId();
		userRepository.deleteById(userRepository.maxUserId());
		
		assertThat(userRepository.existsById(id)).isFalse();
	}

}
