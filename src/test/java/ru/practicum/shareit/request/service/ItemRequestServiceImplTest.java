package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.mapper.item.ItemRepositoryMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.mapper.ItemRequestRepositoryMapper;
import ru.practicum.shareit.request.mapper.ItemRequestRepositoryMapperImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemRequestServiceImplTest {
    @Mock
    ItemRequestRepository itemRequestRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    ItemRepositoryMapper itemRepositoryMapper;

    @Mock
    ItemRequestRepositoryMapper mapper;

    @Mock
    UserService userService;

    @InjectMocks
    private ItemRequestServiceImpl service;

    User user;
    ItemRequest itemRequest;
    ItemRequestEntity itemRequestEntity;
    Item item;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John", "jonh@mail.ru"); // Создайте экземпляр User с необходимыми данными
        itemRequest = new ItemRequest(1L, "Описание", user, null, null); // Создайте экземпляр ItemRequest с необходимыми данными
        itemRequestEntity = new ItemRequestEntity(); // Создайте экземпляр ItemRequestEntity с необходимыми данными
        item = new Item(1L, "Дрель", "Ударная", true, user, null, null, null,null,null); // Создайте экземпляр Item с необходимыми данными

        when(userService.getById(anyLong())).thenReturn(user);
        when(mapper.toItemRequestEntity(any(ItemRequest.class))).thenReturn(itemRequestEntity);
        when(mapper.toItemRequest(any(ItemRequestEntity.class))).thenReturn(itemRequest);
    }

    @Test
    void create() {
        when(itemRequestRepository.save(any(ItemRequestEntity.class))).thenReturn(itemRequestEntity);

        ItemRequest expected = service.create(itemRequest);

        assertEquals(expected, itemRequest);
    }

    @Test
    void getUserRequests() {
        long userId = 1;
        List<ItemRequest> itemRequests = List.of(itemRequest);
        List<ItemEntity> itemEntities = List.of(new ItemEntity());
        List<ItemRequestEntity> itemRequestEntities = List.of(itemRequestEntity);
        when(itemRequestRepository.findAllByRequestorId(any(Long.class))).thenReturn(itemRequestEntities);
        when(itemRepository.findAllByRequestId(any(Long.class))).thenReturn(itemEntities);
        when(itemRepositoryMapper.toItem(any(ItemEntity.class))).thenReturn(item);

        List<ItemRequest> expectedItemRequests = service.getUserRequests(userId);

        assertEquals(expectedItemRequests, itemRequests);
    }

    @Test
    void getAllItemRequest() {
        long userId = 1;
        List<ItemRequest> itemRequests = List.of(itemRequest);
        Pageable pageable = PageRequest.of(0 / 10, 10);
        List<ItemRequestEntity> itemRequestEntities = List.of(itemRequestEntity);

        when(itemRequestRepository.findAllByRequestorIdNot(any(Long.class), any(Pageable.class))).thenReturn(itemRequestEntities);

        List<ItemRequest> expectedItemRequests = service.getAllItemRequest(userId, pageable);
        assertEquals(expectedItemRequests, itemRequests);
    }

    @Test
    void getById() {
        long userId = 1;
        long requestId = 1;
        List<ItemEntity> itemEntities = List.of(new ItemEntity());
        when(itemRepository.findAllByRequestId(any(Long.class))).thenReturn(itemEntities);
        when(itemRequestRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(itemRequestEntity));

        ItemRequest expected = service.getById(userId, requestId);

        assertEquals(expected, itemRequest);
    }

    @Test
    void repositoryMapperConverterTest() {
        mapper = new ItemRequestRepositoryMapperImpl();
        itemRequest = mapper.toItemRequest(itemRequestEntity);

        itemRequestEntity = mapper.toItemRequestEntity(itemRequest);
    }
}