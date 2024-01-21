package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class RequestUserDto {
    @NotEmpty(message = "Имя пользователя не может быть пустым")
    @NotBlank
    private String name;
    @NotEmpty
    @Email(message = "Почта не валидна", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
}
