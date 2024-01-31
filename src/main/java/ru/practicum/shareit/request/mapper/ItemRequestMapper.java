package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.item.ResponseItemForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestWithProposalDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemRequestMapper {

    ResponseItemRequestDto toResponseItemRequestDto(ItemRequest itemRequest);

    @Mapping(target = "requestor.id", source = "userId")
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    ItemRequest toItemRequest(RequestItemRequestDto requestItemRequestDto, Long userId);

    @Mapping(target = "requestId", source = "item.request.id")
    ResponseItemForRequest itemToResponseItemForRequest(Item item);

    List<ResponseItemForRequest> itemsToResponseItemsForRequest(List<Item> items);

    ResponseItemRequestWithProposalDto toResponseItemRequestWithProposalDto(ItemRequest itemRequest);
}
