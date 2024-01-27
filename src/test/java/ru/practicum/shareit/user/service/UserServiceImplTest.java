package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UserRepositoryMapper mapper;

    UserService userService;

    User user = new User(1, "Евгений", "user@test.ru");

    @Test
    void getAll() {

    }

    @Test
    void create() {
        userService = new UserServiceImpl(userRepository, mapper);
        Mockito.when(userService.create(user))
                .thenReturn(user);

        User saved = userService.create(user);

        Assertions.assertEquals(user, saved);
    }

    @Test
    void update() {
    }

    @Test
    void getById() {
    }

    @Test
    void delete() {
    }
}