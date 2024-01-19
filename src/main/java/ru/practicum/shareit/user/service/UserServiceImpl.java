package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.PatchUserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserRepositoryMapper mapper;

    @Override
    public List<User> getAll() {
        return userRepository.findAll().stream()
                .map(mapper::toUser)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public User create(User user) {
        return mapper.toUser(userRepository.save(mapper.toUserEntity(user)));
    }

    @Transactional
    @Override
    public User update(PatchUserDto userDto) {
        UserEntity entity = mapper.toUserEntity(getById(userDto.getId()));

        return mapper.toUser(userRepository.save(mapper.toUser(userDto, entity)));
    }

    @Override
    public User getById(long userId) {
        return mapper.toUser(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id = %d не существует", userId))));
    }

    @Transactional
    @Override
    public void delete(long userId) {
        getById(userId);
        userRepository.deleteById(userId);
    }
}
