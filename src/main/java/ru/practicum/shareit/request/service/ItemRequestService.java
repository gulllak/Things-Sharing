package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest create(ItemRequest itemRequest);

    List<ItemRequest> getUserRequests(long userId);

    List<ItemRequest> getAllItemRequest(long userId, int from, int size);

    ItemRequest getById(long userId, long requestId);
}
