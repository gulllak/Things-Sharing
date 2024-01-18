package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Item item);

    Item getById(long itemId);

    List<Item> getAllUserItems(long userId);

    List<Item> itemSearch(long userId, String searchString);

    Item update(Item item);
}
