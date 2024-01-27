package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest create(ItemRequest itemRequest);

    List<ItemRequest> getUserRequests(long userId);

    List<ItemRequest> getAllItemRequest(long userId, Pageable pageable);

    ItemRequest getById(long userId, long requestId);
}
