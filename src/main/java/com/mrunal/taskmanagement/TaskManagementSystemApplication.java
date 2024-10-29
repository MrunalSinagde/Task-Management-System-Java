package com.mrunal.taskmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;


@SpringBootApplication
public class TaskManagementSystemApplication {

	public static void main(String[] args) {
		
		System.out.println(SpringVersion.getVersion());
		
		SpringApplication.run(TaskManagementSystemApplication.class, args);
	}

}
