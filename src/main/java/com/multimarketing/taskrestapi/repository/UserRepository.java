package com.multimarketing.taskrestapi.repository;

import com.multimarketing.taskrestapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long id);

    @Override
    void delete(User user);

    @Override
    User save(User user);
}
