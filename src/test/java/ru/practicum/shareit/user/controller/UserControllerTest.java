package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.validation.BindException;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @MockBean
    UserService userService;

    @MockBean
    UserMapper mapper;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    private RequestUserDto requestUserDto;

    private User user;

    private User userWithId;

    private ResponseUserDto responseUserDto;

    @BeforeEach
    void setUp() {
        requestUserDto = new RequestUserDto();
        requestUserDto.setName("Jonh");
        requestUserDto.setEmail("Jonh@mail.ru");

        user = new User(0, "Jonh", "Jonh@mail.ru");
        userWithId = new User(1, "Jonh", "Jonh@mail.ru");

        responseUserDto = new ResponseUserDto();
        responseUserDto.setId(1);
        responseUserDto.setName("Jonh");
        responseUserDto.setEmail("Jonh@mail.ru");
    }

    @Test
    void getAll() throws Exception {
        List<ResponseUserDto> responseUserDtoList = List.of(responseUserDto);
        List<User> users = List.of(userWithId);

        when(userService.getAll()).thenReturn(users);

        mvc.perform(get("/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

                //.andExpect(content().json(objectMapper.writeValueAsString(responseUserDtoList)));
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$.id", is(1)));
//                .andExpect(jsonPath("$[0].name", is("Jonh")))
//                .andExpect(jsonPath("$[0].email", is("Jonh@mail.ru")));

        verify(userService, times(1)).getAll();
    }

    @Test
    void getById() throws Exception {
        long userId = 1;
        when(userService.getById(userId)).thenReturn(userWithId);
        when(mapper.toResponseDto(any(User.class))).thenReturn(responseUserDto);

        mvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseUserDto)))
                .andExpect(jsonPath("$.id").value(responseUserDto.getId()))
                .andExpect(jsonPath("$.name").value(responseUserDto.getName()))
                .andExpect(jsonPath("$.email").value(responseUserDto.getEmail()));

        verify(userService, times(1)).getById(userId);
        verify(mapper, times(1)).toResponseDto(any(User.class));
    }

    @Test
    void create() throws Exception {
        when(mapper.toUser(any(RequestUserDto.class))).thenReturn(user);
        when(userService.create(any(User.class))).thenReturn(userWithId);
        when(mapper.toResponseDto(any(User.class))).thenReturn(responseUserDto);


        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(requestUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseUserDto)))
                .andExpect(jsonPath("$.id").value(responseUserDto.getId()))
                .andExpect(jsonPath("$.name").value(responseUserDto.getName()))
                .andExpect(jsonPath("$.email").value(responseUserDto.getEmail()));

        verify(mapper, times(1)).toUser(any(RequestUserDto.class));
        verify(userService, times(1)).create(any(User.class));
        verify(mapper, times(1)).toResponseDto(any(User.class));
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
    void update() {
    }

    @Test
    void delete() {
    }
}