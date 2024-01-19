package ru.practicum.shareit.item.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.dto.PatchItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, BookingMapper.class, CommentMapper.class})
public interface ItemMapper {

    @Mapping(target = "owner.id", source = "userId")
    Item toItem(RequestItemDto requestItemDto, Long userId);

    //@Mapping(target = "id", ignore = true)
    @Mapping(target = "owner.id", source = "userId")
    Item toItem(PatchItemDto patchItemDto, Long userId);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ItemEntity updateItem(ItemEntity item, @MappingTarget ItemEntity entity);

    ResponseItemDto toResponseDto(Item item);

    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    Item toItem(ItemEntity item);

    ItemEntity toItemEntity(Item item);
}
