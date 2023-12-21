package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private long id = 0;
    private static final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item create(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);

        return items.get(item.getId());
    }

    @Override
    public Item getById(long id) {
        Item item = items.get(id);
        if (item == null) {
            throw new EntityNotFoundException(String.format("Вещи с id = %d не существует", id));
        }

        return item;
    }

    @Override
    public List<Item> getAllUserItems(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> itemSearch(String searchString) {
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(searchString.toLowerCase())
                        || item.getDescription().toLowerCase().contains(searchString.toLowerCase()))
                        && item.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public Item update(Item item) {
        items.replace(item.getId(), item);
        return items.get(item.getId());
    }

    private long getNextId() {
        return ++id;
    }
}
