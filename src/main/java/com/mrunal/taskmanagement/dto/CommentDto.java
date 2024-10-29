package com.mrunal.taskmanagement.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.mrunal.taskmanagement.entity.Task;
import com.mrunal.taskmanagement.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

	private int id;
	private String text;
	@CreationTimestamp
	private LocalDateTime createdAt;
	private Task task;
	private User user;

}
