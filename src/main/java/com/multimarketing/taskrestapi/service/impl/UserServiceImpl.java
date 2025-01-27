package com.multimarketing.taskrestapi.service.impl;

import com.multimarketing.taskrestapi.constants.ErrorMessages;
import com.multimarketing.taskrestapi.exception.ApiException;
import com.multimarketing.taskrestapi.model.User;
import com.multimarketing.taskrestapi.repository.UserRepository;
import com.multimarketing.taskrestapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return  userRepository.findAll().stream().toList()
                ;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        user.setId(null);
        User savedUser = userRepository.save(user);

        return savedUser;
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        User userFromRepository = findUserById(id);

        userFromRepository.setFirstName(user.getFirstName());
        userFromRepository.setLastName(user.getLastName());
        userFromRepository.setEmail(user.getEmail());
        userFromRepository.setEnabled(user.getEnabled());

        User savedUser = userRepository.save(userFromRepository);

        return savedUser;
    }

    @Override
    public User findUserById(Long id) {
        User userFromRepository = userRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorMessages.NOT_FOUND_USER_BY_ID + id, HttpStatus.NOT_FOUND)
        );
        return userFromRepository;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User userFromRepository = findUserById(id);
        userRepository.deleteById(userFromRepository.getId());
    }
}
