package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class Comment {
    private long id;
    private String text;
    private Item item;
    private User author;
    private LocalDateTime created;
}
