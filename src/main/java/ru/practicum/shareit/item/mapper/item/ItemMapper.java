package ru.practicum.shareit.item.mapper.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.dto.PatchItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.mapper.comment.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, BookingMapper.class, CommentMapper.class})
public interface ItemMapper {

    @Mapping(target = "owner.id", source = "userId")
    Item toItem(RequestItemDto requestItemDto, Long userId);

    @Mapping(target = "owner.id", source = "userId")
    Item toItem(PatchItemDto patchItemDto, Long userId);

    ResponseItemDto toResponseDto(Item item);
}
