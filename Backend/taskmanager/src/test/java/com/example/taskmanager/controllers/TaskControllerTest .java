package com.example.taskmanager.controllers;

import com.example.taskmanager.models.Task;
import com.example.taskmanager.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Tarea 1");
        task1.setDescription("Descripción de la tarea 1");

        task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Tarea 2");
        task2.setDescription("Descripción de la tarea 2");
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskService.getAllTasks()).thenReturn(tasks);

        List<Task> response = taskController.getAllTasks();

        assertEquals(2, response.size());
        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void testGetTaskById_Success() {
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task1));

        ResponseEntity<Task> response = taskController.getTaskById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task1, response.getBody());
        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskService.getTaskById(3L)).thenReturn(Optional.empty());

        ResponseEntity<Task> response = taskController.getTaskById(3L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(taskService, times(1)).getTaskById(3L);
    }

    @Test
    void testCreateTask() {
        when(taskService.createTask(any(Task.class))).thenReturn(task1);

        Task response = taskController.createTask(task1);

        assertNotNull(response);
        assertEquals("Tarea 1", response.getTitle());
        verify(taskService, times(1)).createTask(task1);
    }

    @Test
    void testUpdateTask() {
        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(task1);

        ResponseEntity<Task> response = taskController.updateTask(1L, task1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task1, response.getBody());
        verify(taskService, times(1)).updateTask(1L, task1);
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskService).deleteTask(1L);

        ResponseEntity<Void> response = taskController.deleteTask(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(taskService, times(1)).deleteTask(1L);
    }
}
