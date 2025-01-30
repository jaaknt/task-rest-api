package com.multimarketing.taskrestapi.repository;

import com.multimarketing.taskrestapi.model.Task;
import com.multimarketing.taskrestapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Override
    Task save(Task task);

    @Override
    Optional<Task> findById(Long id);

    @Override
    List<Task> findAll();

    @Override
    void deleteById(Long id);
}
