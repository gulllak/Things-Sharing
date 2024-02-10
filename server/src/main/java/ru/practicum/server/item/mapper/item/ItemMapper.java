package ru.practicum.server.item.mapper.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.server.item.dto.item.PatchItemDto;
import ru.practicum.server.item.dto.item.RequestItemDto;
import ru.practicum.server.item.mapper.comment.CommentMapper;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.booking.mapper.BookingMapper;
import ru.practicum.server.item.dto.item.ResponseItemDto;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.user.mapper.UserMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class, BookingMapper.class, CommentMapper.class})
public interface ItemMapper {

    @Mapping(target = "owner.id", source = "userId")
    @Mapping(target = "request", expression = "java(mapToRequest(requestItemDto.getRequestId()))")
    Item toItem(RequestItemDto requestItemDto, Long userId);

    @Mapping(target = "owner.id", source = "userId")
    Item toItem(PatchItemDto patchItemDto, Long userId);

    ResponseItemDto toResponseDto(Item item);

    default ItemRequest mapToRequest(Long requestId) {
        if (requestId == null) {
            return null;
        }
        ItemRequest request = new ItemRequest(null, null,null,null,null);
        request.setId(requestId);
        return request;
    }

}
