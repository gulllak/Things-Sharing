package ru.practicum.server.request.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ResponseItemRequestDto {
    private long id;
    private String description;
    private LocalDateTime created;
}
