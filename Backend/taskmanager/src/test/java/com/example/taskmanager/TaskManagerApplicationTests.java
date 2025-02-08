package com.example.taskmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = TaskManagerApplication.class)
@SpringBootApplication(scanBasePackages = "com.example.taskmanager")
class TaskManagerApplicationTests {

	@Test
	void contextLoads() {
	}

}
