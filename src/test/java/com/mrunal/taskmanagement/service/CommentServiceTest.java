package com.mrunal.taskmanagement.service;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrunal.taskmanagement.dto.CommentDto;
import com.mrunal.taskmanagement.entity.Comment;
import com.mrunal.taskmanagement.entity.CustomUserDetails;
import com.mrunal.taskmanagement.entity.User;
import com.mrunal.taskmanagement.exception.CommentNotFoundException;
import com.mrunal.taskmanagement.exception.NotAuthorisedException;
import com.mrunal.taskmanagement.repository.CommentRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
	
	@InjectMocks
	private CommentService commentService;
	
	@Mock
	private CommentRepository commentRepository;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@Mock
	private Authentication authentication;
	
	private Comment comment1;
	private Comment comment2;
	private CommentDto commentDto1;
	private CommentDto commentDto2;
	private CustomUserDetails userDetails;
	private User user;
	
	@BeforeEach
	public void init() {
		user = new User();
		user.setId(1);
		
		comment1 = new Comment();
		comment1.setText("comment1");
		comment1.setUser(user);
		
		commentDto1 = new CommentDto();
		commentDto1.setText("comment1");
		commentDto1.setUser(user);
		
		comment2 = new Comment();
		comment2.setText("text2");
		
		commentDto2 = new CommentDto();
		commentDto2.setText("text2");
		
		userDetails = new CustomUserDetails(user);
		
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void createTest() {
		when(commentRepository.save(any(Comment.class))).thenReturn(comment1);
		
		when(objectMapper.convertValue(comment1, CommentDto.class)).thenReturn(commentDto1);
		
		when(objectMapper.convertValue(commentDto1, Comment.class)).thenReturn(comment1);
		
		CommentDto createCommentDto = commentService.createComment(commentDto1);
		
		assertNotNull(createCommentDto);
		assertEquals("comment1", createCommentDto.getText());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testReadAll() throws CommentNotFoundException {
		List<Comment> comments = new ArrayList<>();
		
		comments.add(comment1);
		comments.add(comment2);
		
		List<CommentDto> commentDtos = new ArrayList<>();
		
		commentDtos.add(commentDto1);
		commentDtos.add(commentDto2);
		
		when(commentRepository.findAll()).thenReturn(comments);
		
		when(objectMapper.convertValue(any(List.class), any(TypeReference.class))).thenReturn(commentDtos);
		
		List<CommentDto> commentsTest = commentService.getAllComments();
		
		assertThat(commentsTest.size()).isGreaterThan(0);
		
		assertEquals(commentDtos.get(0).getText(), "comment1");
		
	}
	
	@Test
	public void testRead() throws CommentNotFoundException {
		when(commentRepository.findById(anyInt())).thenReturn(Optional.of(comment1));
		
		when(objectMapper.convertValue(comment1, CommentDto.class)).thenReturn(commentDto1);
		
		CommentDto commentDto = commentService.getCommentById(1);
		
		assertNotNull(commentDto);
		assertEquals("comment1", commentDto.getText());	
	}
	
	@Test
	public void testUpdate() throws CommentNotFoundException, NotAuthorisedException {
		when(authentication.getPrincipal()).thenReturn(userDetails);
		
		when(commentRepository.findById(anyInt())).thenReturn(Optional.of(comment1));
		
		when(commentRepository.save(any(Comment.class))).thenReturn(comment1);
		
		when(objectMapper.convertValue(comment1, CommentDto.class)).thenReturn(commentDto1);
		
		comment1.setText("updatedcomment");
		commentDto1.setText("updatedcomment");
		
		CommentDto updatedCommentDto = commentService.updateCommentById(commentDto1, 1, authentication);
		
		assertNotNull(updatedCommentDto);
		assertEquals("updatedcomment", updatedCommentDto.getText());	
	}
	
	@Test
	public void testDelete() {
		when(commentRepository.findById(anyInt())).thenReturn(Optional.of(comment1));
		
		doNothing().when(commentRepository).deleteById(anyInt());
		
		when(authentication.getPrincipal()).thenReturn(userDetails);

        assertDoesNotThrow(() -> commentService.deleteCommentById(1,authentication));
	}

}
