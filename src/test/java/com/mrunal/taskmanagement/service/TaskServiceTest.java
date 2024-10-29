package com.mrunal.taskmanagement.service;

import org.mockito.InjectMocks;


import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrunal.taskmanagement.dto.TaskDto;
import com.mrunal.taskmanagement.entity.Status;
import com.mrunal.taskmanagement.entity.Task;
import com.mrunal.taskmanagement.entity.User;
import com.mrunal.taskmanagement.exception.TaskNotFoundException;
import com.mrunal.taskmanagement.repository.TaskRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
	
	@InjectMocks
	private TaskService taskService;
	
	@Mock
	private TaskRepository taskRepository;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@Mock
	private Authentication authentication;
	
	private Task task1;
	private Task task2;
	private TaskDto taskDto1;
	private TaskDto taskDto2;
	private User user;
	
	@BeforeEach
	public void init() {
		user = new User();
		user.setId(1);
		
		task1 = new Task();
		task1.setName("task1");
		task1.setDescription("description1");
		task1.setStatus(Status.IN_PROGRESS);
		task1.setAssignee(user);
		
		taskDto1 = new TaskDto();
		taskDto1.setName("task1");
		taskDto1.setDescription("description1");
		taskDto1.setStatus(Status.IN_PROGRESS);
		
		task2 = new Task();
		task2.setName("task2");
		task2.setDescription("description2");
		task2.setStatus(Status.IN_PROGRESS);
		
		taskDto2 = new TaskDto();
		taskDto2.setName("task2");
		taskDto2.setDescription("description2");
		taskDto2.setStatus(Status.IN_PROGRESS);
		
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void createTest() {
		when(taskRepository.save(any(Task.class))).thenReturn(task1);
		
		when(objectMapper.convertValue(taskDto1, Task.class)).thenReturn(task1);
		
		when(objectMapper.convertValue(task1, TaskDto.class)).thenReturn(taskDto1);
		
		TaskDto createTaskDto = taskService.createTask(taskDto1);
		
		assertNotNull(createTaskDto);
		
		assertEquals("task1", createTaskDto.getName());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testReadAll() throws TaskNotFoundException {
		List<Task> tasks = new ArrayList<>();
		
		tasks.add(task1);
		tasks.add(task2);
		
		List<TaskDto> taskDtos = new ArrayList<>();
		
		taskDtos.add(taskDto1);
		taskDtos.add(taskDto2);
		
		when(taskRepository.findAll()).thenReturn(tasks);
		
		when(objectMapper.convertValue(any(List.class), any(TypeReference.class))).thenReturn(taskDtos);
		
		List<TaskDto> tasksTest = taskService.getAllTasks();
		
		assertThat(tasksTest.size()).isGreaterThan(0);
		
		assertEquals(tasksTest.get(0).getName(), "task1");
	}
	
	@Test
	public void testRead() throws TaskNotFoundException {
		
		when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task1));
		
		when(objectMapper.convertValue(task1, TaskDto.class)).thenReturn(taskDto1);
		
		TaskDto taskDto = taskService.getTaskById(1);
		
		assertNotNull(taskDto);
		
		assertEquals(taskDto.getName(), "task1");	
	}
	
	@Test
	public void testUpdate() throws TaskNotFoundException {
		
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task1));
        
        when(taskRepository.save(any(Task.class))).thenReturn(task1);
        
        when(objectMapper.convertValue(task1, TaskDto.class)).thenReturn(taskDto1);
        
        task1.setStatus(Status.COMPLETED);
        taskDto1.setStatus(Status.COMPLETED);
        
        TaskDto updateTaskDto = taskService.updateTaskById(taskDto1, 1);
        
        assertNotNull(updateTaskDto);
        
        assertEquals(Status.COMPLETED ,updateTaskDto.getStatus() );
	}
	
	@Test
	public void testDelete() {
		
		when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task1));
		
		
        doNothing().when(taskRepository).deleteById(anyInt());

        assertDoesNotThrow(() -> taskService.deleteTaskById(1));
	}

}
