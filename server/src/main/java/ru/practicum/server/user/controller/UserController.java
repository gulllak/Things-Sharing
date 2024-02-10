package ru.practicum.server.user.controller;

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
import ru.practicum.server.user.dto.PatchUserDto;
import ru.practicum.server.user.dto.RequestUserDto;
import ru.practicum.server.user.dto.ResponseUserDto;
import ru.practicum.server.user.mapper.UserMapper;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;

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
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public ResponseUserDto getById(@PathVariable("userId") long userId) {
        return mapper.toResponseDto(userService.getById(userId));
    }

    @PostMapping
    public ResponseUserDto create(@RequestBody RequestUserDto requestUserDto) {
        User user = mapper.toUser(requestUserDto);

        return mapper.toResponseDto(userService.create(user));
    }

    @PatchMapping("/{userId}")
    public ResponseUserDto update(@PathVariable("userId") long userId,
                                  @RequestBody PatchUserDto patchUserDto) {
        patchUserDto.setId(userId);

        return mapper.toResponseDto(userService.update(patchUserDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("userId") long userId) {
        userService.delete(userId);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
