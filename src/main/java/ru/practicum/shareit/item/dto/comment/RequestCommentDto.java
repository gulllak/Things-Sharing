package ru.practicum.shareit.item.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class RequestCommentDto {
    @NotEmpty(message = "Комментарий не может быть пустым")
    @NotBlank
    private String text;
}
