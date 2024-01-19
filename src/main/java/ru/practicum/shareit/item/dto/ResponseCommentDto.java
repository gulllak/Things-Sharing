package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ResponseCommentDto {
    private long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
