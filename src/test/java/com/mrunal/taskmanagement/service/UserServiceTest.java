package com.mrunal.taskmanagement.service;


import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrunal.taskmanagement.dto.UserDto;
import com.mrunal.taskmanagement.entity.CustomUserDetails;
import com.mrunal.taskmanagement.entity.Role;
import com.mrunal.taskmanagement.entity.User;
import com.mrunal.taskmanagement.exception.NotAuthorisedException;
import com.mrunal.taskmanagement.exception.UserNotFoundException;
import com.mrunal.taskmanagement.repository.UserRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@Mock
	private Authentication authentication;
	
	private UserDto userDto1 ;
	private UserDto userDto2;
	private User user1;
	private User user2;
	private CustomUserDetails userDetails;
	
	
	@BeforeEach
	public void setUp() throws Exception {
//		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		
		user1 = new User();
		user1.setUsername("user1");
		user1.setEmail("testemail1@gmail.com");
		user1.setPassword("password");
		user1.setRole(Role.ROLE_USER);
		
		userDto1 = new UserDto();
        userDto1.setUsername("user1");
        userDto1.setEmail("testemail1@gmail.com");
		userDto1.setRole(Role.ROLE_USER);
		
		user2 = new User();
		user2.setUsername("user2");
		user2.setEmail("testemail2@gmail.com");
		user2.setPassword("password");
		user2.setRole(Role.ROLE_USER);
		
		userDto2 = new UserDto();
		userDto2.setUsername("user2");
		userDto2.setEmail("testemail2@gmail.com");
		userDto2.setRole(Role.ROLE_USER);
		
		userDetails = new CustomUserDetails(user1);
		userDetails.getUser().setId(1);
		
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void createTest() throws NotAuthorisedException {
	
//		Mockito.mock(UserDto.class);
//		Mockito.mock(User.class);
//		User user=new User();
//		user.setId(12);
//		user.setEmail("mail");
//		
//		UserDto responseDto = new UserDto();
//		responseDto.setEmail("mail");
//		responseDto.setId(12);
		when(userRepository.save(any(User.class))).thenReturn(user1);
////		when(objectMapper.convertValue(any(User.class), UserDto.class)).thenReturn(responseDto);
//		
//		responseDto = userService.createUser(user);
//		assertNotNull(responseDto);
		
        when(objectMapper.convertValue(user1,  UserDto.class)).thenReturn(userDto1);
        when(objectMapper.convertValue(userDto1, User.class)).thenReturn(user1) ;

        UserDto createUserDto = userService.createUser(userDto1);

        assertNotNull(createUserDto);
        assertEquals("user1", userDto1.getUsername());
    }
		
	
	@SuppressWarnings("unchecked")
	@Test
	public void testReadAll() throws UserNotFoundException {
		List<User> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);
		
		List<UserDto> userDtos = new ArrayList<>();
		userDtos.add(userDto1);
		userDtos.add(userDto2);
		
		when(userRepository.findAll()).thenReturn(users);
		
		when(objectMapper.convertValue(any(List.class), any(TypeReference.class))).thenReturn(userDtos);
		
		
		List<UserDto> usersTest = userService.getAllUsers();
		
		assertThat(usersTest.size()).isGreaterThan(0);
		
		assertEquals(usersTest.get(usersTest.size() - 1).getEmail(), "testemail2@gmail.com" );
	}
	
	@Test
	public void testRead() throws UserNotFoundException, NotAuthorisedException {
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(user1));
		
		when(authentication.getPrincipal()).thenReturn(userDetails);
		
		when(objectMapper.convertValue(user1 , UserDto.class)).thenReturn(userDto1);
		
		UserDto userDto = userService.getUserById(1, authentication);
		
		assertNotNull(userDto);
		assertEquals(userDto.getUsername(), "user1");
	}
	
	@Test
	public void testUpdate() throws UserNotFoundException, NotAuthorisedException {
		
		when(authentication.getPrincipal()).thenReturn(userDetails);
		
		when(userRepository.save(any(User.class))).thenReturn(user1);
		
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(user1));
		
		when(objectMapper.convertValue(user1,  UserDto.class)).thenReturn(userDto1);
		
		user1.setRole(Role.ROLE_ADMIN);
		
		userDto1.setRole(Role.ROLE_ADMIN);
		
		UserDto updatedUserDto = userService.updateUserById(userDto1, 1, authentication);
		
		assertNotNull(updatedUserDto);
		
		assertEquals(Role.ROLE_ADMIN, updatedUserDto.getRole());
	}
	
	@Test
    public void testDeleteUser_Success() {
		
		when(authentication.getPrincipal()).thenReturn(userDetails);
		
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(user1));

        doNothing().when(userRepository).deleteById(anyInt());

        assertDoesNotThrow(() -> userService.deleteUserById(1,authentication));
    }

}
