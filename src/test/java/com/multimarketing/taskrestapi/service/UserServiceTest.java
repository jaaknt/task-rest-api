package com.multimarketing.taskrestapi.service;

import com.multimarketing.taskrestapi.constants.ErrorMessages;
import com.multimarketing.taskrestapi.enums.TaskStatus;
import com.multimarketing.taskrestapi.exception.ApiException;
import com.multimarketing.taskrestapi.model.Task;
import com.multimarketing.taskrestapi.model.User;
import com.multimarketing.taskrestapi.repository.UserRepository;
import com.multimarketing.taskrestapi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private Long id;
    private User user;
    private User repositoryUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id  = 100L;

        user = new User();
        user.setId(id);
        user.setFirstName("Jack");
        user.setLastName("London");
        user.setEnabled(true);
        user.setEmail("jack.london@gmail.com");

        repositoryUser = new User();
        repositoryUser.setId(id);
        repositoryUser.setFirstName("John");
        repositoryUser.setLastName("Lennon");
        repositoryUser.setEnabled(false);
        repositoryUser.setEmail("john.lennon@gmail.com");

    }

    @Test
    void testCreateUser_whenCreateUser_shouldReturnUser() {

        when(userRepository.save(user)).thenReturn(user);

        User expectedResponse = user;

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(userRepository).save(user);
    }

    @Test
    void testFindAllUsers_whenUserExist_shouldReturnListOfUsers() {

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> expectedResponse = List.of(user);

        List<User> result = userService.findAllUsers();

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(userRepository).findAll();
    }

    @Test
    void testFindUserById_whenUserExist_shouldReturnUser() {

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User expectedResponse = user;

        User result = userService.findUserById(id);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(userRepository).findById(id);
    }

    @Test
    void testFindUserById_whenUserNotExist_shouldThrowApiException() {

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            userService.findUserById(id);
        });

        assertEquals(ErrorMessages.NOT_FOUND_USER_BY_ID + id, apiException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, apiException.getHttpStatus());
    }

    @Test
    void testUpdateUser_whenUserExist_shouldReturnUser() {

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(repositoryUser);

        User expectedResponse = repositoryUser;
        User result = userService.updateUser(id, user);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(userRepository).findById(id);
        verify(userRepository).save(user);

     }

    @Test
    void testDeleteUser_whenUserExist_ShouldReturnNothing() {

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.deleteUser(id);

        verify(userRepository).findById(id);
        verify(userRepository).deleteById(id);
    }
}
