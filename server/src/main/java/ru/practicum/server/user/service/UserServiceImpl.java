package ru.practicum.server.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.exception.EntityNotFoundException;
import ru.practicum.server.user.entity.UserEntity;
import ru.practicum.server.user.mapper.UserRepositoryMapper;
import ru.practicum.server.user.repository.UserRepository;
import ru.practicum.server.user.dto.PatchUserDto;
import ru.practicum.server.user.model.User;

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
        UserEntity entity = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id = %d не существует", userDto.getId())));

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
