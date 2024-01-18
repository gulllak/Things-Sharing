package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.PatchUserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User create(User user) {
        return userRepository.save(user);
    }
    @Transactional
    @Override
    public User update(PatchUserDto userDto) {
        User entity = getById(userDto.getId());

        return userRepository.save(mapper.toUser(userDto, entity));
    }

    @Override
    public User getById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id = %d не существует", userId)));
    }

    @Transactional
    @Override
    public void delete(long userId) {
        getById(userId);
        userRepository.deleteById(userId);
    }
}
