package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item create(Item item) {
        userRepository.getById(item.getOwner());

        return itemRepository.create(item);
    }

    @Override
    public Item getById(long id) {
        return itemRepository.getById(id);
    }

    @Override
    public List<Item> getAllUserItems(long userId) {
        userRepository.getById(userId);

        return itemRepository.getAllUserItems(userId);
    }

    @Override
    public List<Item> itemSearch(long userId, String searchString) {
        userRepository.getById(userId);

        return searchString.isBlank() ? new ArrayList<>() : itemRepository.itemSearch(searchString);
    }

    @Override
    public Item update(Item item) {
        userRepository.getById(item.getOwner());

        return itemRepository.update(item);
    }
}
