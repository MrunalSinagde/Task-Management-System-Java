package com.mrunal.taskmanagement.controller;

import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.mrunal.taskmanagement.exception.CommentNotFoundException;
import com.mrunal.taskmanagement.exception.NotAuthorisedException;
import com.mrunal.taskmanagement.service.CommentService;

@RestController
@RequestMapping("/comment")
public class CommentController {
	
	Logger logger = LogManager.getLogger(TaskManagementSystemApplication.class);
	
	@Autowired
	CommentService commentService;
	
	@GetMapping("/")
	public ResponseEntity<List<CommentDto>> getAllComments() throws CommentNotFoundException {
		logger.info("Inside getAllComments of CommentController");
		
		return new ResponseEntity<>(commentService.getAllComments(),HttpStatus.OK);
	}
	
	@GetMapping("/getComment/{id}")
	public ResponseEntity<CommentDto> getCommentById(@PathVariable int id) throws CommentNotFoundException {
		logger.info("Inside getCommentById of CommentController");
		
		return new ResponseEntity<>(commentService.getCommentById(id),HttpStatus.OK);
	}
	
	@PostMapping(value = "/setComment", consumes = {"application/json"} )
	public  ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto) {
		logger.info("Inside createComment of CommentController");
		
		return new ResponseEntity<>(commentService.createComment(commentDto),HttpStatus.OK);
	}
	
	@PutMapping(value = "/updateComment/{id}", consumes = {"application/json"})
	public ResponseEntity<CommentDto> updateComment(@PathVariable int id, @RequestBody CommentDto commentDto, Authentication authentication) throws CommentNotFoundException, NotAuthorisedException {
		logger.info("Inside updateComment of CommentController");
	
		return new ResponseEntity<>(commentService.updateCommentById(commentDto,id,authentication),HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/deleteComment/{id}")
	public ResponseEntity<String> deleteCommentById(@PathVariable int id, Authentication authentication) throws NotAuthorisedException, CommentNotFoundException {
		logger.info("Inside deleteCommentById of CommentController");
		
		String result = commentService.deleteCommentById(id,authentication);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/getMyComments")
	public ResponseEntity<List<CommentDto>> getMyComments(Authentication authentication) throws CommentNotFoundException {
		logger.info("Inside getMyComments of CommentController");
		
		return new ResponseEntity<>(commentService.getCommentsByUser(authentication),HttpStatus.OK);
	} 

}
