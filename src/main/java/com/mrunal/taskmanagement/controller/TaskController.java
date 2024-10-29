package com.mrunal.taskmanagement.controller;

import java.util.List;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.mrunal.taskmanagement.dto.CommentDto;
import com.mrunal.taskmanagement.dto.TaskDto;
import com.mrunal.taskmanagement.exception.CommentNotFoundException;
import com.mrunal.taskmanagement.exception.TaskNotFoundException;
import com.mrunal.taskmanagement.service.CommentService;
import com.mrunal.taskmanagement.service.TaskService;

@RestController
@RequestMapping("/task")
public class TaskController {
	
	Logger logger = LogManager.getLogger(TaskManagementSystemApplication.class);
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private CommentService commentService;
	
	@GetMapping("/")
	public ResponseEntity<List<TaskDto>> getAllTasks() throws TaskNotFoundException {
		logger.info("Inside getAllTasks of TaskController");
		
		return new ResponseEntity<>(taskService.getAllTasks(),HttpStatus.OK);
	}
	
	@GetMapping("/getTask/{id}")
	public ResponseEntity<TaskDto> getTaskById(@PathVariable int id) throws TaskNotFoundException {
		logger.info("Inside getTaskById of TaskController");
		
		return new ResponseEntity<>(taskService.getTaskById(id),HttpStatus.OK);
	}
	
	@PostMapping(value = "/setTask", consumes = {"application/json"} )
	public  ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
		logger.info("Inside createTask of TaskController");
		
		return new ResponseEntity<>(taskService.createTask(taskDto),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/updateTask/{id}")
	public ResponseEntity<TaskDto> updateTask(@PathVariable int id, @RequestBody TaskDto taskDto) throws TaskNotFoundException {
		logger.info("Inside updateTask of TaskController");
	
		return new ResponseEntity<>(taskService.updateTaskById(taskDto,id),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/deleteTask/{id}")
	public ResponseEntity<String> deleteTaskById(@PathVariable int id) throws TaskNotFoundException {
		logger.info("Inside deleteTaskById of TaskController");
		
		String result = taskService.deleteTaskById(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/{taskId}/comments")
	public ResponseEntity<List<CommentDto>> getAllCommentsByTaskId(@PathVariable int taskId, Authentication authentication) throws CommentNotFoundException{
		logger.info("Inside getAllCommentsByTaskId of CommentController");
		
		return new ResponseEntity<>(commentService.getAllCommentsByTaskId(taskId,authentication),HttpStatus.OK);
	}
}
