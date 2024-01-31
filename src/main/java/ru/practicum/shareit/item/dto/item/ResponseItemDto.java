package ru.practicum.shareit.item.dto.item;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.ResponseBookingForItemDto;
import ru.practicum.shareit.item.dto.comment.ResponseCommentDto;

import java.util.List;

@Setter
@Getter
public class ResponseItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private ResponseBookingForItemDto lastBooking;
    private ResponseBookingForItemDto nextBooking;
    private List<ResponseCommentDto> comments;
    private Long requestId;
}
