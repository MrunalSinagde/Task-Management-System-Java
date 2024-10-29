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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mrunal.taskmanagement.TaskManagementSystemApplication;
import com.mrunal.taskmanagement.dto.CommentDto;
import com.mrunal.taskmanagement.entity.Comment;
import com.mrunal.taskmanagement.entity.CustomUserDetails;
import com.mrunal.taskmanagement.entity.Role;
import com.mrunal.taskmanagement.exception.CommentNotFoundException;
import com.mrunal.taskmanagement.exception.NotAuthorisedException;
import com.mrunal.taskmanagement.repository.CommentRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentService {

	Logger logger = LogManager.getLogger(TaskManagementSystemApplication.class);

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Transactional
	public CommentDto createComment(CommentDto commentDto) {
		logger.info("Inside createComment of CommentService");
		Comment comment = objectMapper.convertValue(commentDto, Comment.class);

		Comment savedComment = commentRepository.save(comment);
		return objectMapper.convertValue(savedComment, CommentDto.class);
	}

	public List<CommentDto> getAllComments() throws CommentNotFoundException {
		logger.info("Inside getAllComments of CommentService");

		List<Comment> comments = commentRepository.findAll();
		TypeReference<List<CommentDto>> typeReference = new TypeReference<List<CommentDto>>() {
		};
		List<CommentDto> commentDtoList = objectMapper.convertValue(comments, typeReference);

		if (commentDtoList.isEmpty()) {
			throw new CommentNotFoundException("Currently, there are no comments in the database");
		}
		return commentDtoList;
	}

	public CommentDto getCommentById(int id) throws CommentNotFoundException {
		logger.info("Inside getCommentById of CommentService");

		Optional<Comment> optionalComment = commentRepository.findById(id);

		if (optionalComment.isEmpty()) {
			throw new CommentNotFoundException("The comment with id: " + id + "is not present in the database");
		}
		return objectMapper.convertValue(optionalComment.get(), CommentDto.class);
	}

	@Transactional
	public CommentDto updateCommentById(CommentDto updatedComment, int id, Authentication authentication)
			throws CommentNotFoundException, NotAuthorisedException {
		logger.info("Inside updateCommentById of CommentService");

		Optional<Comment> optionalComment = commentRepository.findById(id);
		if (optionalComment.isEmpty()) {
			throw new CommentNotFoundException("The comment with id: " + id + "is not present i  the database");
		}
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		Comment existingComment = optionalComment.get();

		if (userDetails.getUser().getId() != existingComment.getUser().getId()
				&& (userDetails.getUser().getRole().equals(Role.ROLE_USER))) {
			throw new NotAuthorisedException("You are not authorised to edit this comment");
		}

		if (updatedComment.getText() != null)
			existingComment.setText(updatedComment.getText());

		if (updatedComment.getCreatedAt() != null)
			existingComment.setCreatedAt(updatedComment.getCreatedAt());

		if (updatedComment.getTask() != null)
			existingComment.setTask(updatedComment.getTask());

		if (updatedComment.getUser() != null)
			existingComment.setUser(updatedComment.getUser());

		Comment savedComment = commentRepository.save(existingComment);
		return objectMapper.convertValue(savedComment, CommentDto.class);
	}

	public String deleteCommentById(int id, Authentication authentication)
			throws NotAuthorisedException, CommentNotFoundException {
		logger.info("Inside deleteCommentById of CommentService");

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Optional<Comment> optionalComment = commentRepository.findById(id);
		if (optionalComment.isEmpty()) {
			throw new CommentNotFoundException("Comment with id: " + id + "is not present in the database");
		}

		Comment existingComment = optionalComment.get();

		if (userDetails.getUser().getId() != existingComment.getUser().getId()
				&& (userDetails.getUser().getRole().equals(Role.ROLE_USER))) {
			throw new NotAuthorisedException("You are not authorised to delete this comment");
		}

		commentRepository.deleteById(id);
		return "Comment with id " + id + " is deleted";
	}

	public List<CommentDto> getAllCommentsByTaskId(int taskId, Authentication authentication)
			throws CommentNotFoundException {
		logger.info("Inside getAllCommentsByTaskId of CommentService");

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		List<Comment> comments = commentRepository.getAllCommentsByTaskId(taskId, userDetails.getUser().getId());
		if (comments.isEmpty()) {
			throw new CommentNotFoundException("There are no comments on this task yet");
		}

		TypeReference<List<CommentDto>> typeReference = new TypeReference<List<CommentDto>>() {
		};
		objectMapper = JsonMapper.builder().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).build();
		objectMapper.registerModule(new JavaTimeModule());
		List<CommentDto> commentDtoList = objectMapper.convertValue(comments, typeReference);

		return commentDtoList;
	}

	public List<CommentDto> getCommentsByUser(Authentication authentication) throws CommentNotFoundException {
		logger.info("Inside getCommentsByUser of CommentService");

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		
		List<Comment> comments = commentRepository.getAllCommentsByUserId(userDetails.getUser().getId());
		
		if (comments.isEmpty()) {
			throw new CommentNotFoundException("There are no comments by this user yet");
		}

		TypeReference<List<CommentDto>> typeReference = new TypeReference<List<CommentDto>>() {
		};
		objectMapper = JsonMapper.builder().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).build();
		objectMapper.registerModule(new JavaTimeModule());
		List<CommentDto> commentDtoList = objectMapper.convertValue(comments, typeReference);

		return commentDtoList;
	}

}
