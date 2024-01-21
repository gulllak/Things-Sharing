package ru.practicum.shareit.item.mapper.item;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.booking.mapper.BookingRepositoryMapper;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.mapper.comment.CommentRepositoryMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserRepositoryMapper.class, BookingRepositoryMapper.class, CommentRepositoryMapper.class})
public interface ItemRepositoryMapper {
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    Item toItem(ItemEntity item);

    ItemEntity toItemEntity(Item item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ItemEntity updateItem(ItemEntity item, @MappingTarget ItemEntity entity);

}
