package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.PatchUserDto;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    public List<ResponseUserDto> getAll() {
        return userService.getAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public ResponseUserDto getById(@PathVariable("userId") long userId) {
        return mapper.toDto(userService.getById(userId));
    }

    @PostMapping
    public ResponseUserDto create(@RequestBody @Valid RequestUserDto requestUserDto) {
        User user = mapper.toUser(requestUserDto);

        return mapper.toDto(userService.create(user));
    }

    @PatchMapping("/{userId}")
    public ResponseUserDto update(@PathVariable("userId") long userId,
                          @RequestBody PatchUserDto patchUserDto) {
        validatePatchUserDto(patchUserDto);
        patchUserDto.setId(userId);

        return mapper.toDto(userService.update(patchUserDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("userId") long userId) {
        userService.delete(userId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private void validatePatchUserDto(PatchUserDto patchUserDto) {
        if(patchUserDto.getName() != null && (patchUserDto.getName().isEmpty() || patchUserDto.getName().isBlank())) {
            throw new ValidationException("Имя пользователя не может быть пустым");
        }

        if(patchUserDto.getEmail() != null
                && (patchUserDto.getEmail().isEmpty()
                || patchUserDto.getEmail().isBlank()
                || !patchUserDto.getEmail().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"))) {
            throw new ValidationException("Почта пользователя не может быть пустой и должна соотвествовать формату example@example.com");
        }
    }
}
