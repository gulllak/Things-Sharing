package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.PatchUserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.mapper.UserRepositoryMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    @Mock
    UserRepository userRepository;

    @Mock
    UserRepositoryMapper mapper;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    User user;

    @Mock
    UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(userEntity));
        when(mapper.toUser(userEntity)).thenReturn(user);

        List<User> users = userService.getAll();

        verify(userRepository, times(1)).findAll();
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    void create() {
        when(mapper.toUserEntity(user)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(mapper.toUser(userEntity)).thenReturn(user);

        User createdUser = userService.create(user);

        verify(userRepository, times(1)).save(userEntity);
        assertEquals(user, createdUser);
    }

    @Test
    void getByIdCorrect() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(mapper.toUser(userEntity)).thenReturn(user);

        User foundUser = userService.getById(userId);

        verify(userRepository).findById(userId);
        verify(mapper).toUser(userEntity);
        assertEquals(user, foundUser);
    }

    @Test
    void getById_WhenNotFound_ShouldThrowException() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getById(userId));

        assertTrue(exception.getMessage().contains(String.format("Пользователя с id = %d не существует", userId)));
    }

    @Test
    void repositoryMapperConverterTest() {
        mapper = new UserRepositoryMapperImpl();
        user = mapper.toUser(userEntity);
        userEntity = mapper.toUserEntity(user);

        PatchUserDto patchUserDto = new PatchUserDto();
        patchUserDto.setName("update");

        userEntity = mapper.toUser(patchUserDto, userEntity);
        assertEquals(userEntity.getName(), "update");

        userEntity = null;

        user = mapper.toUser(userEntity);
        assertNull(user);
    }
}