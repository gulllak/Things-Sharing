package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private long id;
    @NotEmpty(message = "Имя вещи не может быть пустым")
    @NotBlank
    private String name;
    @NotEmpty(message = "Описание вещи не может быть пустым")
    @NotBlank
    private String description;
    @NotNull(message = "Необходимо указать статус доступности товара")
    private Boolean available;
}
