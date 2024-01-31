package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.comment.RequestCommentDto;
import ru.practicum.shareit.item.dto.comment.ResponseCommentDto;
import ru.practicum.shareit.item.dto.item.PatchItemDto;
import ru.practicum.shareit.item.dto.item.RequestItemDto;
import ru.practicum.shareit.item.dto.item.ResponseItemDto;
import ru.practicum.shareit.item.mapper.comment.CommentMapper;
import ru.practicum.shareit.item.mapper.comment.CommentMapperImpl;
import ru.practicum.shareit.item.mapper.item.ItemMapper;
import ru.practicum.shareit.item.mapper.item.ItemMapperImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@Import({ItemMapperImpl.class, CommentMapperImpl.class})
class ItemControllerTest {
    @MockBean
    ItemService itemService;

    @MockBean
    ItemMapper itemMapper;

    @MockBean
    CommentMapper commentMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    Item item;

    Item itemCreated;

    User user;

    RequestItemDto requestItemDto;

    ResponseItemDto responseItemDto;

    @BeforeEach
    void setUp() {
        user = new User(1, "Jonh", "Jonh@mail.ru");
        item = new Item(null, "Дрель", "Стробящая", true, user, null, null, null, null, null);
        itemCreated = new Item(1L, "Дрель", "Стробящая", true, user, null, null, null, null, null);
        requestItemDto = new RequestItemDto();
        requestItemDto.setName("Дрель");
        requestItemDto.setDescription("Стробящая");
        requestItemDto.setAvailable(true);

        responseItemDto = new ResponseItemDto();
        responseItemDto.setId(1L);
        responseItemDto.setName("Дрель");
        responseItemDto.setDescription("Стробящая");
        responseItemDto.setAvailable(true);
    }

    @Test
    void createCorrect() throws Exception {
        long userId = 1;
        when(itemService.create(item)).thenReturn(itemCreated);
        when(itemMapper.toResponseDto(itemCreated)).thenReturn(responseItemDto);
        when(itemMapper.toItem(requestItemDto, userId)).thenReturn(item);

        mvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-SHARER-USER-ID", userId)
                .content(objectMapper.writeValueAsString(requestItemDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(responseItemDto)));

        verify(itemService, times(1)).create(item);
    }

    @Test
    void createWithout_X_Sharer_User_Id() throws Exception {
        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestItemDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createNotExistOwnerId() throws Exception {
        long userId = 1;
        when(itemService.create(item)).thenThrow(EntityNotFoundException.class);
        when(itemMapper.toItem(requestItemDto, userId)).thenReturn(item);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-SHARER-USER-ID", userId)
                        .content(objectMapper.writeValueAsString(requestItemDto)))
                .andExpect(status().is4xxClientError());

        verify(itemService, times(1)).create(item);
    }

    @Test
    void createWithoutAvailable() throws Exception {
        requestItemDto.setAvailable(null);
        long userId = 1;

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-SHARER-USER-ID", userId)
                        .content(objectMapper.writeValueAsString(requestItemDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));

        verify(itemService, times(0)).create(item);
    }

    @Test
    void createWithEmptyName() throws Exception {
        requestItemDto.setName("");
        long userId = 1;

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-SHARER-USER-ID", userId)
                        .content(objectMapper.writeValueAsString(requestItemDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));

        verify(itemService, times(0)).create(item);
    }

    @Test
    void createWithEmptyDescription() throws Exception {
        requestItemDto.setDescription("");
        long userId = 1;

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-SHARER-USER-ID", userId)
                        .content(objectMapper.writeValueAsString(requestItemDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));

        verify(itemService, times(0)).create(item);
    }

    @Test
    void getItemByIdCorrect() throws Exception {
        long itemId = 1;
        long userId = 1;

        when(itemService.getById(userId, itemId)).thenReturn(itemCreated);
        when(itemMapper.toResponseDto(itemCreated)).thenReturn(responseItemDto);

        mvc.perform(get("/items/{itemId}", itemId)
                        .header("X-SHARER-USER-ID", userId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(responseItemDto)));

        verify(itemService, times(1)).getById(userId, itemId);
    }

    @Test
    void getItemByIdIncorrect() throws Exception {
        long itemId = 100;
        long userId = 1;

        when(itemService.getById(userId, itemId)).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/items/{itemId}", itemId)
                        .header("X-SHARER-USER-ID", userId))
                .andExpect(status().is4xxClientError());

        verify(itemService, times(1)).getById(userId, itemId);
    }

    @Test
    void getAllUserItems() throws Exception {
        long userId = 1;
        int from = 0;
        int size = 10;

        List<Item> items = List.of(itemCreated);
        List<ResponseItemDto> responseItemDtos = List.of(responseItemDto);

        when(itemService.getAllUserItems(userId, from, size)).thenReturn(items);
        when(itemMapper.toResponseDto(any(Item.class))).thenReturn(responseItemDto);

        mvc.perform(get("/items")
                        .header("X-SHARER-USER-ID", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(responseItemDtos)))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Дрель")))
                .andExpect(jsonPath("$[0].description", is("Стробящая")));

        verify(itemService, times(1)).getAllUserItems(userId, from, size);
    }

    @Test
    void itemSearch() throws Exception {
        long userId = 1;
        String text = "Рел";
        int from = 0;
        int size = 10;

        List<Item> items = List.of(itemCreated);
        List<ResponseItemDto> responseItemDtos = List.of(responseItemDto);

        when(itemService.itemSearch(userId, text, from, size)).thenReturn(items);
        when(itemMapper.toResponseDto(any(Item.class))).thenReturn(responseItemDto);

        mvc.perform(get("/items/search")
                        .header("X-SHARER-USER-ID", userId)
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(responseItemDtos)))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(itemService, times(1)).itemSearch(userId, text, from, size);
    }

    @Test
    void updateCorrect() throws Exception {
        long userId = 1;
        long itemId = 1;
        PatchItemDto patchItemDto = new PatchItemDto();
        patchItemDto.setId(1L);
        patchItemDto.setName("update");
        patchItemDto.setDescription("update");
        patchItemDto.setAvailable(true);

        responseItemDto.setName("update");
        responseItemDto.setDescription("update");

        when(itemMapper.toItem(any(PatchItemDto.class), any(Long.class))).thenReturn(item);
        when(itemService.update(any(Item.class))).thenReturn(itemCreated);
        when(itemMapper.toResponseDto(any(Item.class))).thenReturn(responseItemDto);

        mvc.perform(patch("/items/{itemId}", itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-SHARER-USER-ID", userId)
                .content(objectMapper.writeValueAsString(patchItemDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(responseItemDto)))
                .andExpect(jsonPath("$.name").value("update"))
                .andExpect(jsonPath("$.description").value("update"));
    }

    @Test
    void updateWithout_X_Sharer_User_Id() throws Exception {
        long itemId = 1;

        PatchItemDto patchItemDto = new PatchItemDto();
        patchItemDto.setId(1L);
        patchItemDto.setName("update");
        patchItemDto.setDescription("update");
        patchItemDto.setAvailable(true);

        mvc.perform(patch("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchItemDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateFailEmptyName() throws Exception {
        long itemId = 1;
        long userId = 1;

        PatchItemDto patchItemDto = new PatchItemDto();
        patchItemDto.setId(1L);
        patchItemDto.setName("");
        patchItemDto.setDescription("update");
        patchItemDto.setAvailable(true);

        mvc.perform(patch("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-SHARER-USER-ID", userId)
                        .content(objectMapper.writeValueAsString(patchItemDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateFailEmptyDescription() throws Exception {
        long itemId = 1;
        long userId = 1;

        PatchItemDto patchItemDto = new PatchItemDto();
        patchItemDto.setId(1L);
        patchItemDto.setName("update");
        patchItemDto.setDescription("");
        patchItemDto.setAvailable(true);

        mvc.perform(patch("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-SHARER-USER-ID", userId)
                        .content(objectMapper.writeValueAsString(patchItemDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createComment() throws Exception {
        long itemId = 1;
        long userId = 1;
        RequestCommentDto requestCommentDto = new RequestCommentDto();
        requestCommentDto.setText("Новый коммент");

        ResponseCommentDto responseCommentDto = new ResponseCommentDto();
        responseCommentDto.setId(1);
        responseCommentDto.setText("Новый коммент");
        responseCommentDto.setAuthorName("Jonh");
        responseCommentDto.setCreated(LocalDateTime.of(1,1,1,1,1));

        Comment comment = new Comment(1, "Новый коммент", itemCreated, user, LocalDateTime.of(1,1,1,1,1));

        when(commentMapper.toComment(any(RequestCommentDto.class), any(Long.class), any(Long.class))).thenReturn(comment);
        when(itemService.createComment(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toResponseComment(any(Comment.class))).thenReturn(responseCommentDto);

        mvc.perform(post("/items/{itemId}/comment", itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-SHARER-USER-ID", userId)
                .content(objectMapper.writeValueAsString(requestCommentDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(responseCommentDto)))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("Новый коммент"))
                .andExpect(jsonPath("$.authorName").value("Jonh"));
    }
}