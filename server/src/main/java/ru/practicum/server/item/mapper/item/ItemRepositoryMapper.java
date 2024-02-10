package ru.practicum.server.item.mapper.item;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.server.item.entity.ItemEntity;
import ru.practicum.server.item.mapper.comment.CommentRepositoryMapper;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.booking.mapper.BookingRepositoryMapper;
import ru.practicum.server.user.mapper.UserRepositoryMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserRepositoryMapper.class, BookingRepositoryMapper.class, CommentRepositoryMapper.class})
public interface ItemRepositoryMapper {
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "requestId", source = "request.id")
    Item toItem(ItemEntity item);

    ItemEntity toItemEntity(Item item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ItemEntity updateItem(ItemEntity item, @MappingTarget ItemEntity entity);

}
