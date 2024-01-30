package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.mapper.BookingRepositoryMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.entity.CommentEntity;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.mapper.comment.CommentRepositoryMapper;
import ru.practicum.shareit.item.mapper.item.ItemRepositoryMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.mapper.ItemRequestRepositoryMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRepositoryMapper itemMapper;
    @Mock
    private CommentRepositoryMapper commentMapper;
    @Mock
    private BookingRepositoryMapper bookingMapper;
    @Mock
    private ItemRequestRepositoryMapper itemRequestRepositoryMapper;

    @InjectMocks
    private ItemServiceImpl itemService;
    User owner;
    UserEntity userEntity;

    Item item;
    ItemEntity itemEntity;
    ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        itemRequest = new ItemRequest(1L, "Описание", owner, null, null);
        owner = new User(1L, "John", "jonh@mail.ru");
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("John");
        userEntity.setEmail("jonh@mail.ru");

        item = new Item(1L, "Дрель", "Ударная", true, owner, null, null, null,null,null);
        itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setName("Дрель");
        itemEntity.setDescription("Ударная");
        itemEntity.setOwner(userEntity);
    }

    @Test
    void testCreateItemWithValidOwnerAndNoRequest() {
        when(userService.getById(any(Long.class))).thenReturn(owner);
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(itemEntity);
        when(itemMapper.toItem(any(ItemEntity.class))).thenReturn(item);
        when(itemMapper.toItemEntity(any(Item.class))).thenReturn(itemEntity);

        Item createdItem = itemService.create(item);
        assertEquals(createdItem, item);
    }

    @Test
    void testCreateItemWithValidOwnerAndRequest() {
        item.setRequest(itemRequest);

        when(userService.getById(any(Long.class))).thenReturn(owner);
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(itemEntity);
        when(itemMapper.toItem(any(ItemEntity.class))).thenReturn(item);
        when(itemMapper.toItemEntity(any(Item.class))).thenReturn(itemEntity);

        Item createdItem = itemService.create(item);
        assertEquals(createdItem, item);
    }

    @Test
    void getItemById() {
        long userId = 1;
        long itemId = 1;

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(itemEntity));
        when(itemMapper.toItem(any(ItemEntity.class))).thenReturn(item);
        when(itemMapper.toItemEntity(any(Item.class))).thenReturn(itemEntity);

        Item expectedItem = itemService.getById(userId, itemId);
        assertEquals(expectedItem, item);
    }

    @Test
    void getItemByIdNotExist() {
        long userId = 1;
        long itemId = 2;

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> itemService.getById(userId, itemId));

        assertTrue(exception.getMessage().contains(String.format("Вещи с id = %d не существует", itemId)));
    }

    @Test
    void getAllUserItems() {
        long userId = 1;
        Pageable pageable = PageRequest.of(0 / 10, 10);

        List<ItemEntity> itemEntities = List.of(itemEntity);

        when(userService.getById(any(Long.class))).thenReturn(owner);
        when(itemRepository.findAllByOwnerIdOrderById(any(Long.class), any(Pageable.class))).thenReturn(itemEntities);
        when(itemMapper.toItem(any(ItemEntity.class))).thenReturn(item);
        when(itemMapper.toItemEntity(any(Item.class))).thenReturn(itemEntity);

        List<Item> expectedList = itemService.getAllUserItems(userId, pageable);

        assertEquals(expectedList, List.of(item));
    }

    @Test
    void itemSearchCorrect() {
        long userId = 1;
        String text = "рел";
        Pageable pageable = PageRequest.of(0 / 10, 10);
        List<ItemEntity> itemEntities = List.of(itemEntity);

        when(itemRepository.search(any(String.class), any(Pageable.class))).thenReturn(itemEntities);
        when(itemMapper.toItem(any(ItemEntity.class))).thenReturn(item);

        List<Item> expectedList = itemService.itemSearch(userId, text, pageable);
        assertEquals(expectedList, List.of(item));
    }

    @Test
    void itemSearchWithEmptyText() {
        long userId = 1;
        String text = "";
        Pageable pageable = PageRequest.of(0 / 10, 10);

        List<Item> expectedList = itemService.itemSearch(userId, text, pageable);
        assertEquals(expectedList, new ArrayList<>());
    }

    @Test
    void updateItem() {
        when(userService.getById(any(Long.class))).thenReturn(owner);
        when(itemMapper.toItem(any(ItemEntity.class))).thenReturn(item);
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(itemEntity));
        when(itemMapper.toItemEntity(any(Item.class))).thenReturn(itemEntity);
        when(itemMapper.updateItem(any(ItemEntity.class), any(ItemEntity.class))).thenReturn(itemEntity);
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(itemEntity);

        Item expectedItem = itemService.update(item);

        assertEquals(expectedItem, item);
    }

    @Test
    void createComment() {
        Comment comment = new Comment(1L, "Супер вещь", item, owner, null);
        CommentEntity entity = new CommentEntity();

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(itemEntity));
        when(itemMapper.toItem(any(ItemEntity.class))).thenReturn(item);
        when(itemMapper.toItemEntity(any(Item.class))).thenReturn(itemEntity);
        when(userService.getById(any(Long.class))).thenReturn(owner);
        when(userService.getById(any(Long.class))).thenReturn(owner);
        when(commentMapper.toCommentEntity(any(Comment.class))).thenReturn(entity);
        when(commentMapper.toComment(any(CommentEntity.class))).thenReturn(comment);
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(entity);
        when(bookingRepository.findFirstByBookerIdAndItemIdAndEndBefore(any(Long.class), any(Long.class), any(LocalDateTime.class))).thenReturn(Optional.of(new BookingEntity()));

        Comment expectedComment = itemService.createComment(comment);

        assertEquals(expectedComment, comment);
    }

    @Test
    void createCommentIfNotAccess() {
        Comment comment = new Comment(1L, "Супер вещь", item, owner, null);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> itemService.createComment(comment));

        assertTrue(exception.getMessage().contains(String.format("Пользователь с id = %d не имеет права оставить комментарий к вещи с id = %d", 1, 1)));
    }




}