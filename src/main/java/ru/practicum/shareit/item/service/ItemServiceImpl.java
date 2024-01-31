package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.mapper.BookingRepositoryMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.entity.CommentEntity;
import ru.practicum.shareit.item.mapper.comment.CommentRepositoryMapper;
import ru.practicum.shareit.item.mapper.item.ItemRepositoryMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.mapper.ItemRequestRepositoryMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepositoryMapper itemMapper;
    private final CommentRepositoryMapper commentMapper;
    private final BookingRepositoryMapper bookingMapper;
    private final ItemRequestRepositoryMapper itemRequestRepositoryMapper;

    @Transactional
    @Override
    public Item create(Item item) {
        item.setOwner(userService.getById(item.getOwner().getId()));

        if (item.getRequest() != null) {
            ItemRequest itemRequest = itemRequestRepositoryMapper.toItemRequest(itemRequestRepository.findById(item.getRequest().getId()).orElse(null));
            item.setRequest(itemRequest);
        }
        return itemMapper.toItem(itemRepository.save(itemMapper.toItemEntity(item)));
    }

    @Override
    public Item getById(long userId, long itemId) {
        return itemRepository.findById(itemId)
                .map(itemMapper::toItem)
                .map(item -> {
                    fillItem(userId, item);
                    return item;
                }).orElseThrow(() -> new EntityNotFoundException(String.format("Вещи с id = %d не существует", itemId)));
    }

    @Override
    public List<Item> getAllUserItems(long userId, int from, int size) {
        isUserExists(userId);

        return itemRepository.findAllByOwnerIdOrderById(userId, getPageable(from, size)).stream()
                .map(itemMapper::toItem)
                .peek(item -> fillItem(userId, item))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> itemSearch(long userId, String searchString, int from, int size) {
        isUserExists(userId);

        return searchString.isBlank()
                ? new ArrayList<>()
                : itemRepository.search(searchString, getPageable(from, size)).stream()
                .map(itemMapper::toItem)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Item update(Item item) {
        item.setOwner(userService.getById(item.getOwner().getId()));

        Item entity = getById(item.getOwner().getId(), item.getId());

        if (item.getOwner().getId() != entity.getOwner().getId()) {
            throw new AccessDeniedException(String.format("Пользователь с id = %d не имеет права изменять вещь с id = %d",
                    item.getOwner().getId(), item.getId()));
        }
        ItemEntity savedEntity = itemMapper.updateItem(itemMapper.toItemEntity(item), itemMapper.toItemEntity(entity));
        return itemMapper.toItem(itemRepository.save(savedEntity));
    }

    @Transactional
    @Override
    public Comment createComment(Comment comment) {
        bookingRepository.findFirstByBookerIdAndItemIdAndEndBefore(comment.getAuthor().getId(), comment.getItem().getId(), LocalDateTime.now())
                .orElseThrow(() -> new ValidationException(String.format("Пользователь с id = %d не имеет права оставить комментарий к вещи с id = %d",
                        comment.getAuthor().getId(), comment.getItem().getId())));

        comment.setItem(getById(comment.getAuthor().getId(), comment.getItem().getId()));
        comment.setAuthor(userService.getById(comment.getAuthor().getId()));
        CommentEntity entity = commentMapper.toCommentEntity(comment);

        return commentMapper.toComment(commentRepository.save(entity));
    }

    private void fillItem(long userId, Item item) {
        final ItemEntity entity = itemMapper.toItemEntity(item);
        if (Objects.equals(entity.getOwner().getId(), userId)) {
            final LocalDateTime start = LocalDateTime.now();
            final BookingEntity lastBooking = bookingRepository
                    .findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(entity, start, BookingStatus.APPROVED)
                    .orElse(null);
            final BookingEntity nextBooking = bookingRepository
                    .findFirstByItemAndStartAfterAndStatusOrderByStart(entity, start, BookingStatus.APPROVED)
                    .orElse(null);

            item.setLastBooking(bookingMapper.toBooking(lastBooking));
            item.setNextBooking(bookingMapper.toBooking(nextBooking));
        }
        item.setComments(commentRepository.findAllByItem(entity).stream()
                .map(commentMapper::toComment)
                .collect(Collectors.toList()));
    }

    private void isUserExists(long userId) {
        userService.getById(userId);
    }

    private Pageable getPageable(int from, int size) {
        return PageRequest.of(from / size, size);
    }
}
