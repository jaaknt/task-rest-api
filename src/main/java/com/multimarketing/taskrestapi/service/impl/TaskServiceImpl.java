package com.multimarketing.taskrestapi.service.impl;

import com.multimarketing.taskrestapi.constants.ErrorMessages;
import com.multimarketing.taskrestapi.exception.ApiException;
import com.multimarketing.taskrestapi.model.Task;
import com.multimarketing.taskrestapi.model.User;
import com.multimarketing.taskrestapi.repository.TaskRepository;
import com.multimarketing.taskrestapi.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final String NOT_FOUND_USER_BY_ID = "Not found user by id: ";

    @Override
    @Transactional
    public Task createTask(Task task) {
        task.setId(null);
        Task savedTask = taskRepository.save(task);

        return savedTask;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAllTasks() {
        return  taskRepository.findAll().stream().toList()
                ;
    }

    @Override
    public Task findTaskById(Long id) {
        Task taskFromRepository = taskRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorMessages.NOT_FOUND_TASK_BY_ID + id, HttpStatus.NOT_FOUND)
        );
        return taskFromRepository;
    }


    @Override
    @Transactional
    public Task updateTask(Long taskId, Task newTask) {
        Task taskFromRepository = findTaskById(taskId);

        taskFromRepository.setStatus(newTask.getStatus());
        taskFromRepository.setTitle(newTask.getTitle());
        taskFromRepository.setDescription(newTask.getDescription());
        taskFromRepository.setDueDate(newTask.getDueDate());
        taskFromRepository.setUser(newTask.getUser());

        return taskRepository.save(taskFromRepository);
    }
/*
    @Override
    @Transactional
    public Task updateTaskStatus(Long taskId, String newStatus) {
        Task taskFromRepository = findTaskById(taskId);

        taskFromRepository.setStatus(newStatus);
        return taskRepository.save(taskFromRepository);
    }
*/
    @Override
    @Transactional
    public void deleteTask(Long id) {
        Task taskFromRepository = findTaskById(id);
        taskRepository.deleteById(id);
    }
}
