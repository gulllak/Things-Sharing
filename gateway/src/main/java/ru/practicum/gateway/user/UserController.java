package ru.practicum.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userClient.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable("userId") long userId) {
        log.info("Received user by id: {}", userId);
        ResponseEntity<Object> user = userClient.getById(userId);
        log.info("Response user by id: {}", user);
        return user;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid RequestUserDto requestUserDto) {
        log.info("Received request to create user: {}", requestUserDto);
        ResponseEntity<Object> response = userClient.create(requestUserDto);
        log.info("Response from user client: {}", response);
        return response;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable("userId") long userId,
                                         @RequestBody PatchUserDto patchUserDto) {
        log.info("Received request to update user: {}", patchUserDto);
        validatePatchUserDto(patchUserDto);
        ResponseEntity<Object> updated = userClient.update(userId, patchUserDto);
        log.info("Response from user client: {}", updated);
        return updated;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable("userId") long userId) {
        log.info("Delete user by id: {}", userId);

        return userClient.delete(userId);
    }

    private void validatePatchUserDto(PatchUserDto patchUserDto) {
        if (patchUserDto.getName() != null && (patchUserDto.getName().isEmpty() || patchUserDto.getName().isBlank())) {
            log.error("Validation failed for name: '{}'", patchUserDto.getName());
            throw new ValidationException("Имя пользователя не может быть пустым");
        }

        if (patchUserDto.getEmail() != null
                && (patchUserDto.getEmail().isEmpty()
                || patchUserDto.getEmail().isBlank()
                || !patchUserDto.getEmail().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"))) {
            log.error("Validation failed for email: '{}'", patchUserDto.getEmail());
            throw new ValidationException("Почта пользователя не может быть пустой и должна соотвествовать формату example@example.com");
        }
    }
}
