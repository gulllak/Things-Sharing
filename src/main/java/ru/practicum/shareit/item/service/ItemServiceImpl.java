package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final ItemMapper mapper;

    @Override
    public Item create(Item item) {
        item.setOwner(userService.getById(item.getOwner().getId()));

        return itemRepository.save(item);
    }

    @Override
    public Item getById(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException(String.format("Вещи с id = %d не существует", itemId)));
    }

    @Override
    public List<Item> getAllUserItems(long userId) {
        userService.getById(userId);

        return itemRepository.findAllByOwnerId(userId);
    }

    @Override
    public List<Item> itemSearch(long userId, String searchString) {
        userService.getById(userId);

        return searchString.isBlank() ? new ArrayList<>() : itemRepository.search(searchString);
    }

    @Override
    public Item update(Item item) {
        item.setOwner(userService.getById(item.getOwner().getId()));

        Item entity = getById(item.getId());

        if (item.getOwner().getId() != entity.getOwner().getId()) {
            throw new AccessDeniedException(String.format("Пользователь с id = %d не имеет права изменять вещь с id = %d", item.getOwner().getId(), item.getId()));
        }

        return itemRepository.save(mapper.updateItem(item, entity));
    }
}
