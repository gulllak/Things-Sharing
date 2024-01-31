package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.mapper.item.ItemRepositoryMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.mapper.ItemRequestRepositoryMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    private final ItemRepositoryMapper itemRepositoryMapper;

    private final ItemRequestRepositoryMapper mapper;

    private final UserService userService;

    @Transactional
    @Override
    public ItemRequest create(ItemRequest itemRequest) {
        User user = userService.getById(itemRequest.getRequestor().getId());
        itemRequest.setRequestor(user);

        ItemRequestEntity entity = mapper.toItemRequestEntity(itemRequest);

        return mapper.toItemRequest(itemRequestRepository.save(entity));
    }

    @Override
    public List<ItemRequest> getUserRequests(long userId) {
        isUserExists(userId);

        return itemRequestRepository.findAllByRequestorId(userId).stream()
                .map(mapper::toItemRequest)
                .peek(itemRequest -> {
                    final List<Item> items = itemRepository.findAllByRequestId(itemRequest.getId()).stream()
                            .map(itemRepositoryMapper::toItem)
                            .collect(Collectors.toList());

                    itemRequest.setItems(items);
                }).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequest> getAllItemRequest(long userId, int from, int size) {
        isUserExists(userId);

        return itemRequestRepository.findAllByRequestorIdNot(userId, getPageable(from, size)).stream()
                .map(mapper::toItemRequest)
                .peek(itemRequest -> {
                    final List<Item> items = itemRepository.findAllByRequestId(itemRequest.getId()).stream()
                            .map(itemRepositoryMapper::toItem)
                            .collect(Collectors.toList());

                    itemRequest.setItems(items);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequest getById(long userId, long requestId) {
        isUserExists(userId);

        ItemRequest itemRequest = mapper.toItemRequest(itemRequestRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException("Такого запроса не существует")));

        final List<Item> items = itemRepository.findAllByRequestId(itemRequest.getId()).stream()
                .map(itemRepositoryMapper::toItem)
                .collect(Collectors.toList());
        itemRequest.setItems(items);

        return itemRequest;
    }

    private void isUserExists(long userId) {
        userService.getById(userId);
    }

    private Pageable getPageable(int from, int size) {
        return PageRequest.of(from / size, size, Sort.by("id").descending());
    }
}
