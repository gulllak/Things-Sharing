package ru.practicum.server.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.server.item.dto.item.ResponseItemForRequest;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.request.dto.RequestItemRequestDto;
import ru.practicum.server.request.dto.ResponseItemRequestDto;
import ru.practicum.server.request.dto.ResponseItemRequestWithProposalDto;
import ru.practicum.server.request.model.ItemRequest;

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
