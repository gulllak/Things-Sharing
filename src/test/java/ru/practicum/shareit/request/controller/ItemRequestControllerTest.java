package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.item.ResponseItemForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestWithProposalDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.mapper.ItemRequestMapperImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@Import(ItemRequestMapperImpl.class)
class ItemRequestControllerTest {
    @MockBean
    ItemRequestService itemRequestService;

    @MockBean
    ItemRequestMapper mapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    RequestItemRequestDto requestItemRequestDto;

    ItemRequest createdItemRequest;

    User user;

    Item item;

    ResponseItemRequestWithProposalDto responseItemRequestWithProposalDto;

    @BeforeEach
    void setUp() {
        requestItemRequestDto = new RequestItemRequestDto();
        requestItemRequestDto.setDescription("Add comment from user1");

        user = new User(1, "Jonh", "Jonh@mail.ru");
        item = new Item(null, "Дрель", "Стробящая", true, user, null, null, null, null, null);

        createdItemRequest = new ItemRequest(1L, "Add comment from user1", user, null, List.of(item));

        ResponseItemForRequest responseItemForRequest = new ResponseItemForRequest();
        responseItemForRequest.setId(1L);
        responseItemForRequest.setName("Дрель");
        responseItemForRequest.setDescription("Стробящая");
        responseItemForRequest.setAvailable(true);
        responseItemForRequest.setRequestId(1L);

        responseItemRequestWithProposalDto = new ResponseItemRequestWithProposalDto();
        responseItemRequestWithProposalDto.setId(1);
        responseItemRequestWithProposalDto.setDescription("Add comment from user1");
        responseItemRequestWithProposalDto.setItems(List.of(responseItemForRequest));
    }

    @Test
    void createCommentCorrect() throws Exception {
        long userId = 1;

        ResponseItemRequestDto responseItemRequestDto = new ResponseItemRequestDto();
        responseItemRequestDto.setId(1);
        responseItemRequestDto.setDescription("Add comment from user1");

        when(mapper.toItemRequest(any(RequestItemRequestDto.class), any(Long.class))).thenReturn(createdItemRequest);
        when(itemRequestService.create(any(ItemRequest.class))).thenReturn(createdItemRequest);
        when(mapper.toResponseItemRequestDto(any(ItemRequest.class))).thenReturn(responseItemRequestDto);

        mvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-SHARER-USER-ID", userId)
                .content(objectMapper.writeValueAsString(requestItemRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(responseItemRequestDto)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Add comment from user1")));

        verify(itemRequestService, times(1)).create(createdItemRequest);
    }

    @Test
    void createCommentEmpty() throws Exception {
        long userId = 1;

        requestItemRequestDto.setDescription("");

        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-SHARER-USER-ID", userId)
                        .content(objectMapper.writeValueAsString(requestItemRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));

        verify(itemRequestService, times(0)).create(createdItemRequest);
    }

    @Test
    void getUserRequests() throws Exception {
        long userId = 1;
        List<ItemRequest> itemRequests = List.of(createdItemRequest);
        List<ResponseItemRequestWithProposalDto> response = List.of(responseItemRequestWithProposalDto);

        when(itemRequestService.getUserRequests(any(Long.class))).thenReturn(itemRequests);
        when(mapper.toResponseItemRequestWithProposalDto(any(ItemRequest.class))).thenReturn(responseItemRequestWithProposalDto);

        mvc.perform(get("/requests")
                .header("X-SHARER-USER-ID", userId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is("Add comment from user1")))
                .andExpect(jsonPath("$[0].items[0].id", is(1)));

        verify(itemRequestService, times(1)).getUserRequests(any(Long.class));
    }

    @Test
    void getNotExistUserRequests() throws Exception {
        long userId = 10;

        when(itemRequestService.getUserRequests(any(Long.class))).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/requests")
                        .header("X-SHARER-USER-ID", userId))
                .andExpect(status().is4xxClientError());

        verify(itemRequestService, times(1)).getUserRequests(any(Long.class));
    }

    @Test
    void getAllItemRequest() throws Exception {
        long userId = 1;
        int from = 0;
        int size = 10;

        List<ItemRequest> itemRequests = List.of(createdItemRequest);
        List<ResponseItemRequestWithProposalDto> response = List.of(responseItemRequestWithProposalDto);

        when(itemRequestService.getAllItemRequest(any(Long.class), any(Pageable.class))).thenReturn(itemRequests);
        when(mapper.toResponseItemRequestWithProposalDto(any(ItemRequest.class))).thenReturn(responseItemRequestWithProposalDto);

        mvc.perform(get("/requests/all")
                        .header("X-SHARER-USER-ID", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is("Add comment from user1")))
                .andExpect(jsonPath("$[0].items[0].id", is(1)));

        verify(itemRequestService, times(1)).getAllItemRequest(any(Long.class), any(Pageable.class));
    }

    @Test
    void getById() throws Exception {
        long requestId = 1;
        long userId = 1;

        when(itemRequestService.getById(any(Long.class), any(Long.class))).thenReturn(createdItemRequest);
        when(mapper.toResponseItemRequestWithProposalDto(any(ItemRequest.class))).thenReturn(responseItemRequestWithProposalDto);

        mvc.perform(get("/requests/{requestId}", requestId)
                .header("X-SHARER-USER-ID", userId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(responseItemRequestWithProposalDto)))
                .andExpect(jsonPath("$.description", is("Add comment from user1")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.items[0].id", is(1)));
    }
}