package com.mrunal.taskmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mrunal.taskmanagement.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>{
	
	@Query(value = "SELECT MAX(id) FROM task", nativeQuery = true)
	public int maxTaskId();

	@Query(value = "SELECT * FROM task WHERE assignee_id = :userId", nativeQuery = true)
	public List<Task> getTasksByUserId(int userId);
}
