package ru.practicum.server.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.server.request.entity.ItemRequestEntity;
import ru.practicum.server.request.model.ItemRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemRequestRepositoryMapper {
    ItemRequest toItemRequest(ItemRequestEntity itemRequestEntity);

    ItemRequestEntity toItemRequestEntity(ItemRequest itemRequest);
}
