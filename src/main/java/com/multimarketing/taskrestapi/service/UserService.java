package com.multimarketing.taskrestapi.service;

import com.multimarketing.taskrestapi.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(Long id, User user);

    User findUserById(Long id);

    void deleteUser(Long id);
}
