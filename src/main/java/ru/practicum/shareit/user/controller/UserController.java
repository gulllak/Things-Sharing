package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll().stream()
                .map(this::userToUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable("userId") long id) {
        return userToUserDto(userService.getById(id));
    }

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        User user = userDtoToUser(userDto);

        return userToUserDto(userService.create(user));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable("userId") long id,
                          @RequestBody UserDto userDto) {
        userDto.setId(id);
        User user = userDtoToUser(userDto);

        return userToUserDto(userService.update(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("userId") long id) {
        userService.delete(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private UserDto userToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User userDtoToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
