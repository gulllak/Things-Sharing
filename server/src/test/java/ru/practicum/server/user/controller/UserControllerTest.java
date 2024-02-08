package ru.practicum.server.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindException;
import ru.practicum.server.exception.DuplicateResourceException;
import ru.practicum.server.exception.EntityNotFoundException;
import ru.practicum.server.user.dto.PatchUserDto;
import ru.practicum.server.user.dto.RequestUserDto;
import ru.practicum.server.user.dto.ResponseUserDto;
import ru.practicum.server.user.mapper.UserMapper;
import ru.practicum.server.user.mapper.UserMapperImpl;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@Import(UserMapperImpl.class)
class UserControllerTest {
    @MockBean
    UserService userService;

    @Autowired
    UserMapper mapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserController userController;

    @Autowired
    MockMvc mvc;

    RequestUserDto requestUserDto;

    User user;

    User createdUser;

    ResponseUserDto responseUserDto;

    @BeforeEach
    void setUp() {
        requestUserDto = new RequestUserDto();
        requestUserDto.setName("Jonh");
        requestUserDto.setEmail("Jonh@mail.ru");

        user = new User(0, "Jonh", "Jonh@mail.ru");
        createdUser = new User(1, "Jonh", "Jonh@mail.ru");

        responseUserDto = new ResponseUserDto();
        responseUserDto.setId(1);
        responseUserDto.setName("Jonh");
        responseUserDto.setEmail("Jonh@mail.ru");
    }

    @Test
    void getAll() throws Exception {
        List<ResponseUserDto> responseUserDtoList = List.of(responseUserDto);
        List<User> users = List.of(createdUser);

        when(userService.getAll()).thenReturn(users);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseUserDtoList)))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Jonh")))
                .andExpect(jsonPath("$[0].email", is("Jonh@mail.ru")));

        verify(userService, times(1)).getAll();
    }

    @Test
    void getByIdCorrect() throws Exception {
        long userId = 1;
        when(userService.getById(userId)).thenReturn(createdUser);

        mvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseUserDto)))
                .andExpect(jsonPath("$.id").value(responseUserDto.getId()))
                .andExpect(jsonPath("$.name").value(responseUserDto.getName()))
                .andExpect(jsonPath("$.email").value(responseUserDto.getEmail()));

        verify(userService, times(1)).getById(userId);
    }

    @Test
    void getByIdIncorrect() throws Exception {
        long userId = 99;
        when(userService.getById(userId)).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(userService, times(1)).getById(userId);
    }

    @Test
    void createCorrect() throws Exception {
        when(userService.create(any(User.class))).thenReturn(createdUser);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(requestUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseUserDto)))
                .andExpect(jsonPath("$.id").value(responseUserDto.getId()))
                .andExpect(jsonPath("$.name").value(responseUserDto.getName()))
                .andExpect(jsonPath("$.email").value(responseUserDto.getEmail()));

        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    void createWithEmptyNameShouldGive400() throws Exception {
        requestUserDto.setName("");

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(requestUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));
    }

    @Test
    void createWithEmptyEmailShouldGive400() throws Exception {
        requestUserDto.setEmail("");

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(requestUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));
    }

    @Test
    void updateCorrect() throws Exception {
        createdUser.setName("update");
        createdUser.setEmail("update@mail.ru");

        when(userService.update(any(PatchUserDto.class))).thenReturn(createdUser);

        mvc.perform(patch("/users/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"update\",\"email\":\"update@mail.ru\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("update"))
                .andExpect(jsonPath("$.email").value("update@mail.ru"));

        verify(userService, times(1)).update(any(PatchUserDto.class));
    }

    @Test
    void updateFailEmailExist() throws Exception {
        when(userService.update(any(PatchUserDto.class))).thenThrow(DuplicateResourceException.class);

        mvc.perform(patch("/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"update\",\"email\":\"update@mail.ru\"}"))
                .andExpect(status().is4xxClientError());

        verify(userService, times(1)).update(any(PatchUserDto.class));
    }

    @Test
    void updateFailEmptyName() throws Exception {
        mvc.perform(patch("/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"email\":\"\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateFailIncorrectEmail() throws Exception {
        mvc.perform(patch("/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"name\",\"email\":\"hello\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteCorrect() throws Exception {
        long userId = 1;
        doNothing().when(userService).delete(userId);

        mvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userService).delete(userId);
    }

    @Test
    void deleteNotExistUser() throws Exception {
        long userId = 1;
        doThrow(EntityNotFoundException.class).when(userService).delete(userId);

        mvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().is4xxClientError());

        verify(userService).delete(userId);
    }
}