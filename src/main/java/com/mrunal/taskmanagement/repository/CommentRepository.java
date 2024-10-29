package com.mrunal.taskmanagement.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mrunal.taskmanagement.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{

	@Query(value = "SELECT MAX(id) FROM comment", nativeQuery = true)
	public int maxCommentId();

	@Query(value = "SELECT * FROM comment WHERE task_id = :taskId ORDER BY (user_id = :userId) DESC", nativeQuery = true)
	public List<Comment> getAllCommentsByTaskId(int taskId, int userId);

	@Query(value = "SELECT * FROM comment WHERE user_id = :userId", nativeQuery = true)
	public List<Comment> getAllCommentsByUserId(int userId);

}
