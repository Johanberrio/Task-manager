package com.example.taskmanager.services;

import com.example.taskmanager.models.Task;
import com.example.taskmanager.repositories.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus("Pending");
        task1.setDueDate(createDate(2025, 2, 8));
        task1.setPriority("Alta");

        task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setStatus("Completed");
        task2.setDueDate(createDate(2025, 2, 8));
        task2.setPriority("Baja");
    }

    @Test
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskById_Found() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        Optional<Task> foundTask = taskService.getTaskById(1L);

        assertTrue(foundTask.isPresent());
        assertEquals("Task 1", foundTask.get().getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<Task> foundTask = taskService.getTaskById(3L);

        assertFalse(foundTask.isPresent());
        verify(taskRepository, times(1)).findById(3L);
    }

    @Test
    void testCreateTask() {
        when(taskRepository.save(task1)).thenReturn(task1);

        Task savedTask = taskService.createTask(task1);

        assertNotNull(savedTask);
        assertEquals("Task 1", savedTask.getTitle());
        verify(taskRepository, times(1)).save(task1);
    }

    @Test
    void testUpdateTask_Success() {
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task 1");
        updatedTask.setDescription("Updated Description 1");
        updatedTask.setStatus("In Progress");
        updatedTask.setDueDate(createDate(2025, 2, 8));
        updatedTask.setPriority("Media");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        when(taskRepository.save(any(Task.class))).thenReturn(task1);

        Task result = taskService.updateTask(1L, updatedTask);

        assertNotNull(result);
        assertEquals("Updated Task 1", result.getTitle());
        assertEquals("Updated Description 1", result.getDescription());
        assertEquals("In Progress", result.getStatus());
        assertEquals("Media", result.getPriority());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task1);
    }

    @Test
    void testUpdateTask_NotFound() {
        Task updatedTask = new Task();
        updatedTask.setTitle("Non-Existent Task");

        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> taskService.updateTask(99L, updatedTask));

        assertEquals("Tarea no encontrada", exception.getMessage());
        verify(taskRepository, times(1)).findById(99L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0); 
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
