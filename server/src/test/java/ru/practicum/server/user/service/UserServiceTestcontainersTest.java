package ru.practicum.server.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.BaseTest;
import ru.practicum.server.exception.DuplicateResourceException;
import ru.practicum.server.exception.EntityNotFoundException;
import ru.practicum.server.user.dto.PatchUserDto;
import ru.practicum.server.user.mapper.UserRepositoryMapper;
import ru.practicum.server.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTestcontainersTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepositoryMapper userRepositoryMapper;

    @Transactional
    @Test
    void crateUser() {
        User user = createUser("test@test.com", "test");
        User createdUser = userService.create(user);
        assertNotNull(createdUser);
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Transactional
    @Test
    void createUserWithDuplicateEmail() {
        userService.create(createUser("test@test.com", "test"));
        Exception exception = assertThrows(DuplicateResourceException.class, () -> userService.create(createUser("test@test.com", "test2")));
        assertEquals("Email test@test.com уже существует и должен быть уникальным", exception.getMessage());
    }

    @Transactional
    @Test
    void getUserById_whenUserExists_shouldReturnUser() {
        User createdUser = userService.create(createUser("test@test.com", "test"));
        User userById = userService.getById(createdUser.getId());
        assertNotNull(userById);
        assertEquals(createdUser.getId(), userById.getId());
        assertEquals(createdUser.getName(), userById.getName());
        assertEquals(createdUser.getEmail(), userById.getEmail());
    }


    @Test
    void getUserById_whenUserDoesNotExist() {
        Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.getById(1L));
        assertEquals("Пользователя с id = 1 не существует", exception.getMessage());
    }

    @Transactional
    @Test
    void deleteUserById_whenUserExists_shouldDeleteUser() {
        User createdUser = userService.create(createUser("test@test.com", "test"));
        userService.delete(createdUser.getId());
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getById(createdUser.getId());
        });
    }

    @Transactional
    @Test
    void getAllUsers_whenUserExists_shouldReturnTwoUsers() {
        userService.create(createUser("test@test.com", "test"));
        userService.create(createUser("test2@test.com", "test2"));
        List<User> users = userService.getAll();
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void updateUser_whenUserDoesNotExists() {
        PatchUserDto updateUserDto = new PatchUserDto();
        updateUserDto.setId(1L);
        updateUserDto.setName("updatedTest");
        Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.update(updateUserDto));
        assertEquals("Пользователя с id = 1 не существует", exception.getMessage());
    }

    private User createUser(String email, String name) {
        User user = new User(0L, email, name);
        user.setEmail(email);
        user.setName(name);
        return user;
    }
}