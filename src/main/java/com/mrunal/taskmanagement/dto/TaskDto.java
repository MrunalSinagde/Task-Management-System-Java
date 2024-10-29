package com.mrunal.taskmanagement.dto;

import java.util.List;

import com.mrunal.taskmanagement.entity.Comment;
import com.mrunal.taskmanagement.entity.Status;
import com.mrunal.taskmanagement.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

	private int id;
	private String name;
	private String description;
	private Status status;
	private User assignee;
	private List<Comment> comments;

}
