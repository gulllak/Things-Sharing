package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.PatchUserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User create(User user);

    User update(PatchUserDto userDto);

    User getById(long userId);

    void delete(long userId);
}
