package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestWithProposalDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/requests")
@Validated
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestMapper mapper;

    private final ItemRequestService service;

    @PostMapping
    public ResponseItemRequestDto create(@RequestHeader(value = ItemController.X_SHARER_USER_ID) long userId,
                                         @RequestBody @Valid RequestItemRequestDto requestItemRequestDto) {
        ItemRequest itemRequest = mapper.toItemRequest(requestItemRequestDto, userId);

        return mapper.toResponseItemRequestDto(service.create(itemRequest));
    }

    @GetMapping
    public List<ResponseItemRequestWithProposalDto> getUserRequests(@RequestHeader(value = ItemController.X_SHARER_USER_ID) long userId) {
        return service.getUserRequests(userId).stream()
                .map(mapper::toResponseItemRequestWithProposalDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<ResponseItemRequestWithProposalDto> getAllItemRequest(@RequestHeader(value = ItemController.X_SHARER_USER_ID) long userId,
                                                          @RequestParam(value = "from", defaultValue = "0")
                                                          @Min(value = 0, message = "Начало не может быть отрицательным") int from,
                                                          @RequestParam(value = "size", defaultValue = "10")
                                                              @Min(value = 1, message = "Размер должен быть больше 0") int size) {
        return service.getAllItemRequest(userId, from, size).stream()
                .map(mapper::toResponseItemRequestWithProposalDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    public ResponseItemRequestWithProposalDto getById(@RequestHeader(value = ItemController.X_SHARER_USER_ID) long userId,
                                                      @PathVariable("requestId") long requestId) {
        return mapper.toResponseItemRequestWithProposalDto(service.getById(userId, requestId));
    }

}
