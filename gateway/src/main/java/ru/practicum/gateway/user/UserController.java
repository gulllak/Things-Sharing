package ru.practicum.gateway.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.gateway.exception.ValidationException;
import ru.practicum.gateway.user.dto.PatchUserDto;
import ru.practicum.gateway.user.dto.RequestUserDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userClient.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable("userId") long userId) {
        return userClient.getById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid RequestUserDto requestUserDto) {
        return userClient.create(requestUserDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable("userId") long userId,
                          @RequestBody PatchUserDto patchUserDto) {
        validatePatchUserDto(patchUserDto);

        return userClient.update(userId, patchUserDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable("userId") long userId) {

        return userClient.delete(userId);
    }

    private void validatePatchUserDto(PatchUserDto patchUserDto) {
        if (patchUserDto.getName() != null && (patchUserDto.getName().isEmpty() || patchUserDto.getName().isBlank())) {
            throw new ValidationException("Имя пользователя не может быть пустым");
        }

        if (patchUserDto.getEmail() != null
                && (patchUserDto.getEmail().isEmpty()
                || patchUserDto.getEmail().isBlank()
                || !patchUserDto.getEmail().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"))) {
            throw new ValidationException("Почта пользователя не может быть пустой и должна соотвествовать формату example@example.com");
        }
    }
}
