package com.multimarketing.taskrestapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multimarketing.taskrestapi.constants.ErrorMessages;
import com.multimarketing.taskrestapi.exception.ApiException;
import com.multimarketing.taskrestapi.model.User;
import com.multimarketing.taskrestapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long id;
    private User user;
    private User repositoryUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = 999L;

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
    void testFindAllUsers_whenUsersExists_shouldReturnUsersList() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(user.getLastName()))
                .andExpect(jsonPath("$[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$[0].enabled").value(user.getEnabled()));

        verify(userService).findAllUsers();
    }

    @Test
    void testFindAllUsers_whenNoUsersExist_shouldReturnEmptyList() throws Exception {
        when(userService.findAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(userService).findAllUsers();
    }

    @Test
    void testCreateUser_whenValidRequest_shouldReturnCreatedUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(repositoryUser);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(repositoryUser.getId()))
                .andExpect(jsonPath("$.firstName").value(repositoryUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(repositoryUser.getLastName()))
                .andExpect(jsonPath("$.email").value(repositoryUser.getEmail()))
                .andExpect(jsonPath("$.enabled").value(repositoryUser.getEnabled())) ;

        verify(userService).createUser(user);
    }

    @Test
    void testUpdateUser_whenUserExists_shouldReturnUser() throws Exception {
        when(userService.updateUser(id, user)).thenReturn(repositoryUser);

        mockMvc.perform(put("/users/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(repositoryUser.getId()))
                .andExpect(jsonPath("$.firstName").value(repositoryUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(repositoryUser.getLastName()))
                .andExpect(jsonPath("$.email").value(repositoryUser.getEmail()))
                .andExpect(jsonPath("$.enabled").value(repositoryUser.getEnabled())) ;

        verify(userService).updateUser(id, user);
    }

    @Test
    void testUpdateUser_whenUserNotExists_shouldReturnApiException() throws Exception {

        String errorMessage = ErrorMessages.NOT_FOUND_USER_BY_ID + id;

        when(userService.updateUser(id, user))
                .thenThrow(new ApiException(errorMessage, HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/users/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(errorMessage));

        verify(userService).updateUser(id, user);
    }

    @Test
    void testDeleteUser_whenUserExists_shouldReturnNothing() throws Exception {

        mockMvc.perform(delete("/users/{id}", id)
                        .contentType("application/json")
                        )
                .andExpect(status().isNoContent()) ;

        verify(userService).deleteUser(id);
    }

    @Test
    void testDeleteUser_whenUserNotExists_shouldReturnApiException() throws Exception {
        String errorMessage = ErrorMessages.NOT_FOUND_USER_BY_ID + id;

        doThrow(new ApiException(errorMessage, HttpStatus.NOT_FOUND))
                .when(userService).deleteUser(id);

        mockMvc.perform(delete("/users/{id}", id)
                        .contentType("application/json")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(errorMessage));

        verify(userService).deleteUser(id);
    }

}
