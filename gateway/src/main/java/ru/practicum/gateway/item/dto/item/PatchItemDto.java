package ru.practicum.gateway.item.dto.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PatchItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
}
