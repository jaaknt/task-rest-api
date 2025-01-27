package com.multimarketing.taskrestapi.service;

import com.multimarketing.taskrestapi.model.Task;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);

    Task findTaskById(Long id);

    List<Task> findAllTasks();

    Task updateTask(Long taskId, Task newTask);

    // Task updateTaskStatus(Long taskId, String newStatus);

    void deleteTask(Long id);
}
