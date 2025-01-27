package com.multimarketing.taskrestapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multimarketing.taskrestapi.constants.ErrorMessages;
import com.multimarketing.taskrestapi.enums.TaskStatus;
import com.multimarketing.taskrestapi.exception.ApiException;
import com.multimarketing.taskrestapi.model.Task;
import com.multimarketing.taskrestapi.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @MockitoBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long id;
    private Task task;
    private Task repositoryTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        id = 89L;
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
    void testFindAllTasks_whenTasksExists_shouldReturnTasksList() throws Exception {
        when(taskService.findAllTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(task.getId()))
                .andExpect(jsonPath("$[0].title").value(task.getTitle()))
                .andExpect(jsonPath("$[0].description").value(task.getDescription()))
                .andExpect(jsonPath("$[0].status").value(task.getStatus().toString()))
                .andExpect(jsonPath("$[0].dueDate").value(task.getDueDate()));

        verify(taskService).findAllTasks();
    }

    @Test
    void testFindAllTasks_whenNoTasksExist_shouldReturnEmptyList() throws Exception {
        when(taskService.findAllTasks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(taskService).findAllTasks();
    }

    @Test
    void testUpdateTask_whenTaskExists_shouldReturnTask() throws Exception {
        when(taskService.updateTask(id, task)).thenReturn(repositoryTask);

        mockMvc.perform(put("/tasks/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(repositoryTask.getId()))
                .andExpect(jsonPath("$.title").value(repositoryTask.getTitle()))
                .andExpect(jsonPath("$.description").value(repositoryTask.getDescription()))
                .andExpect(jsonPath("$.status").value(repositoryTask.getStatus().toString()))
                .andExpect(jsonPath("$.dueDate").value(repositoryTask.getDueDate().toString().substring(0,27)))
        ;

        verify(taskService).updateTask(id, task);
    }

    @Test
    void testUpdateTask_whenTaskNotExists_shouldReturnApiException() throws Exception {

        String errorMessage = ErrorMessages.NOT_FOUND_TASK_BY_ID + id;

        when(taskService.updateTask(id, task))
                .thenThrow(new ApiException(errorMessage, HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/tasks/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(errorMessage));

        verify(taskService).updateTask(id, task);
    }

    @Test
    void testDeleteTask_whenTaskExists_shouldReturnNothing() throws Exception {

        mockMvc.perform(delete("/tasks/{id}", id)
                        .contentType("application/json")
                )
                .andExpect(status().isNoContent()) ;

        verify(taskService).deleteTask(id);
    }

    @Test
    void testDeleteTask_whenTaskNotExists_shouldReturnApiException() throws Exception {
        String errorMessage = ErrorMessages.NOT_FOUND_TASK_BY_ID + id;

        doThrow(new ApiException(errorMessage, HttpStatus.NOT_FOUND))
                .when(taskService).deleteTask(id);

        mockMvc.perform(delete("/tasks/{id}", id)
                        .contentType("application/json")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(errorMessage));

        verify(taskService).deleteTask(id);
    }
}
