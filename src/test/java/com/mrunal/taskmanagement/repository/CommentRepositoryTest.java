package com.mrunal.taskmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mrunal.taskmanagement.entity.Comment;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class CommentRepositoryTest {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Test
	@Order(1)
	public void createComment() {
		System.out.println("1.create");
		Comment comment = new Comment();
		comment.setText("test comment");
		
		Comment savedComment = commentRepository.save(comment);
		
		assertNotNull(commentRepository.findById(savedComment.getId()));
	}
	
	@Test
	@Order(2)
	public void testReadAll() {
		System.out.println("2.read all");
		List<Comment> comments = commentRepository.findAll();
		
		assertThat(comments).size().isGreaterThan(0);
	}
	
	@Test
	@Order(3)
	public void testRead() {
		System.out.println("3.read");
		System.out.println(commentRepository.maxCommentId());
		Comment user = commentRepository.findById(commentRepository.maxCommentId()).get();
		assertEquals("test comment", user.getText());
	}
	
	@Test
	@Order(4)
	public void testUpdate() {
		System.out.println("4.update");
		Comment comment = commentRepository.findById(commentRepository.maxCommentId()).get();
		comment.setText("updated text");
		commentRepository.save(comment);
		
		assertNotEquals("test text", commentRepository.findById(commentRepository.maxCommentId()).get().getText());
	}
	
	@Test
	@Order(5)
	public void testDelete() {
		System.out.println("5.delete");
		int id = commentRepository.maxCommentId();
		commentRepository.deleteById(commentRepository.maxCommentId());
		
		assertThat(commentRepository.existsById(id)).isFalse();
	}

}
