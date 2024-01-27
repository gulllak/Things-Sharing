package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemRequestRepositoryMapper {
    ItemRequest toItemRequest(ItemRequestEntity itemRequestEntity);

    ItemRequestEntity toItemRequestEntity(ItemRequest itemRequest);
}
