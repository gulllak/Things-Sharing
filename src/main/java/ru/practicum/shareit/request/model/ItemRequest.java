package ru.practicum.shareit.request.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private Long id;
    private Long description;
    private Long requestor;
    private LocalDateTime created;
}
