package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Item item);

    Item getById(long id);

    List<Item> getAll(long userId);

    List<Item> itemSearch(long userId, String searchString);

    Item update(Item item);
}
