package com.multimarketing.taskrestapi.service;

import com.multimarketing.taskrestapi.constants.ErrorMessages;
import com.multimarketing.taskrestapi.enums.TaskStatus;
import com.multimarketing.taskrestapi.exception.ApiException;
import com.multimarketing.taskrestapi.model.Task;
import com.multimarketing.taskrestapi.repository.TaskRepository;
import com.multimarketing.taskrestapi.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Long id;
    private Task task;
    private Task repositoryTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        id  = 99L;
        task = new Task();
        task.setId(id);
        task.setTitle("Sample Task");
        task.setDescription("Task Description");
        task.setStatus(TaskStatus.CREATED);

        repositoryTask = new Task();
        repositoryTask.setId(id);
        repositoryTask.setTitle("Table Task");
        repositoryTask.setDescription("Task Description 1-2-3");
        repositoryTask.setStatus(TaskStatus.IN_PROGRESS);
        repositoryTask.setDueDate(LocalDateTime.now(Clock.systemUTC()));

    }


    @Test
    void testCreateTask_whenCreateTask_shouldReturnTask() {

        when(taskRepository.save(task)).thenReturn(task);

        Task expectedResponse = task;

        Task result = taskService.createTask(task);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(taskRepository).save(task);
    }

    @Test
    void testFindAllTasks_whenTaskExist_shouldReturnListOfTasks() {

        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<Task> expectedResponse = List.of(task);

        List<Task> result = taskService.findAllTasks();

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(taskRepository).findAll();
    }

    @Test
    void testFindTaskById_whenTaskExist_shouldReturnTask() {

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task expectedResponse = task;

        Task result = taskService.findTaskById(1L);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(taskRepository).findById(1L);
    }

    @Test
    void testFindTaskById_whenTaskNotExist_shouldThrowApiException() {

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            taskService.findTaskById(id);
        });

        assertEquals(ErrorMessages.NOT_FOUND_TASK_BY_ID + id, apiException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, apiException.getHttpStatus());
    }

    @Test
    void testUpdateTask_whenTaskExist_shouldReturnTask() {

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(repositoryTask);

        Task expectedResponse = repositoryTask;
        Task result = taskService.updateTask(id, task);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(taskRepository).findById(id);
        verify(taskRepository).save(task);
    }

    @Test
    void testDeleteTask_whenTaskExist_ShouldReturnNothing() {

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        taskService.deleteTask(id);

        verify(taskRepository).findById(id);
        verify(taskRepository).deleteById(id);
    }
}
