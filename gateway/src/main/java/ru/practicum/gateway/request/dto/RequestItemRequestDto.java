package ru.practicum.gateway.request.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class RequestItemRequestDto {
    @NotEmpty(message = "Описание вещи не может быть пустым")
    @NotBlank
    private String description;
}
