package ru.practicum.server.item.dto.comment;

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
