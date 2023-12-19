package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item create(Item item);

    Item getById(long id);

    List<Item> getAll(long userId);

    List<Item> itemSearch(String searchString);

    Item update(Item item);
}
