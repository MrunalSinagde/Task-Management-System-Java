package com.mrunal.taskmanagement.service;

import java.util.List;


import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrunal.taskmanagement.TaskManagementSystemApplication;
import com.mrunal.taskmanagement.dto.TaskDto;
import com.mrunal.taskmanagement.entity.CustomUserDetails;
import com.mrunal.taskmanagement.entity.Task;
import com.mrunal.taskmanagement.exception.TaskNotFoundException;
import com.mrunal.taskmanagement.repository.TaskRepository;

import jakarta.transaction.Transactional;

@Service
public class TaskService {

	Logger logger = LogManager.getLogger(TaskManagementSystemApplication.class);

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Transactional
	public TaskDto createTask(TaskDto taskDto) {
		logger.info("Inside createTask of TaskService");

		Task task = objectMapper.convertValue(taskDto, Task.class);

		Task savedTask = taskRepository.save(task);
		return objectMapper.convertValue(savedTask, TaskDto.class);
	}

	public List<TaskDto> getAllTasks() throws TaskNotFoundException {
		logger.info("Inside getAllTasks of TaskService");

		List<Task> tasks = taskRepository.findAll();
		TypeReference<List<TaskDto>> typeReference = new TypeReference<List<TaskDto>>() {
		};
		List<TaskDto> taskDtoList = objectMapper.convertValue(tasks, typeReference);
		if (taskDtoList.isEmpty()) {
			throw new TaskNotFoundException("Currently, there no tasks in the database");

		}
		return taskDtoList;
	}

	public TaskDto getTaskById(int id) throws TaskNotFoundException {
		logger.info("Inside getTaskById of TaskService");

		Optional<Task> optionalTask = taskRepository.findById(id);
		if (optionalTask.isEmpty()) {
			throw new TaskNotFoundException("Task not found with id: " + id);
		}
		return objectMapper.convertValue(optionalTask.get(), TaskDto.class);
	}

	@Transactional
	public TaskDto updateTaskById(TaskDto updatedTask, int id) throws TaskNotFoundException {
		logger.info("Inside updateTaskById of TaskService");

		Optional<Task> optionalTask = taskRepository.findById(id);
		if (optionalTask.isEmpty()) {
			throw new TaskNotFoundException("The task with id: " + id + "is not present in the database.");
		}

		Task existingTask = optionalTask.get();

		if (updatedTask.getName() != null)
			existingTask.setName(updatedTask.getName());

		if (updatedTask.getDescription() != null)
			existingTask.setDescription(updatedTask.getDescription());

		if (updatedTask.getStatus() != null)
			existingTask.setStatus(updatedTask.getStatus());

		if (updatedTask.getAssignee() != null)
			existingTask.setAssignee(updatedTask.getAssignee());

		if (updatedTask.getComments() != null)
			existingTask.setComments(updatedTask.getComments());

		Task savedTask = taskRepository.save(existingTask);
		return objectMapper.convertValue(savedTask, TaskDto.class);
	}

	public String deleteTaskById(int id) throws TaskNotFoundException {
		logger.info("Inside deleteTaskById of TaskService");
		
		Optional<Task> optionalTask = taskRepository.findById(id);
		
		if(optionalTask.isEmpty()) {
			throw new TaskNotFoundException("Task with id: "+id+" is not present in the database");
		}

		taskRepository.deleteById(id);
		return "Task with id " + id + " is deleted";
	}

	public List<TaskDto> getTasksByUserId(Authentication authentication) throws TaskNotFoundException {
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		
		List<Task> tasks = taskRepository.getTasksByUserId(userDetails.getUser().getId());
		if(tasks.isEmpty()) {
			throw new TaskNotFoundException("There are no tasks associated to this user currently");
		}
		TypeReference<List<TaskDto>> typeReference = new TypeReference<List<TaskDto>>() {
		};
		return objectMapper.convertValue(tasks, typeReference);	
	}
}
