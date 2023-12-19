package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AccessDeniedException;
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
        if (!existItem(id)) {
            throw new EntityNotFoundException(String.format("Вещи с id = %d не существует", id));
        }

        return items.get(id);
    }

    @Override
    public List<Item> getAll(long userId) {
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
        if (!existItem(item.getId())) {
            throw new EntityNotFoundException(String.format("Вещи с id = %d не существует", item.getId()));
        }

        if (!isItemOwner(item.getId(), item.getOwner())) {
            throw new AccessDeniedException(String.format("Пользователь с id = %d не имеет права изменять вещь с id = %d", item.getOwner(), item.getId()));
        }

        Item updatedItem = getById(item.getId());
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }

        items.replace(item.getId(), updatedItem);
        return items.get(item.getId());
    }

    private boolean existItem(long id) {
        return items.containsKey(id);
    }

    private boolean isItemOwner(long itemId, long ownerId) {
        Item item = getById(itemId);

        return item.getOwner() == ownerId;
    }

    private long getNextId() {
        return ++id;
    }
}
