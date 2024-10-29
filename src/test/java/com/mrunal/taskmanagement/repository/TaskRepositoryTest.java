package com.mrunal.taskmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.mrunal.taskmanagement.entity.Status;
import com.mrunal.taskmanagement.entity.Task;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class TaskRepositoryTest {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Test
	@Order(1)
	public void createTask() {
		Task task = new Task();
		task.setName("test task");
		task.setStatus(Status.IN_PROGRESS);
		task.setDescription("description test");
		
		Task savedTask = taskRepository.save(task);
		
		assertNotNull(taskRepository.findById(savedTask.getId()));
	}
	
	@Test
	@Order(2)
	public void testReadAll() {
		List<Task> tasks = taskRepository.findAll();
		
		assertThat(tasks).size().isGreaterThan(0);
	}
	
	@Test
	@Order(3)
	public void testRead() {
		System.out.println(taskRepository.maxTaskId());
		Task task = taskRepository.findById(taskRepository.maxTaskId()).get();
		assertEquals("test task", task.getName());
	}
	
	@Test
	@Order(4)
	public void testUpdate() {
		Task task = taskRepository.findById(taskRepository.maxTaskId()).get();
		task.setStatus(Status.COMPLETED);
		taskRepository.save(task);
		
		assertNotEquals(Status.IN_PROGRESS, taskRepository.findById(taskRepository.maxTaskId()).get().getStatus());
	}
	
	@Test
	@Order(5)
	public void testDelete() {
		int id = taskRepository.maxTaskId();
		taskRepository.deleteById(taskRepository.maxTaskId());
		
		assertThat(taskRepository.existsById(id)).isFalse();
	}

}
