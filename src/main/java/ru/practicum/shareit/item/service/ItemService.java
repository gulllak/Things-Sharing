package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Item item);

    Item getById(long userId, long itemId);

    List<Item> getAllUserItems(long userId, Pageable pageable);

    List<Item> itemSearch(long userId, String searchString, Pageable pageable);

    Item update(Item item);

    Comment createComment(Comment comment);
}
