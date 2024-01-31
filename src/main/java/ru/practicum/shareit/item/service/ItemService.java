package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Item item);

    Item getById(long userId, long itemId);

    List<Item> getAllUserItems(long userId, int from, int size);

    List<Item> itemSearch(long userId, String searchString, int from, int size);

    Item update(Item item);

    Comment createComment(Comment comment);
}
