package ru.practicum.server.item.dto.item;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.server.item.dto.comment.ResponseCommentDto;
import ru.practicum.server.booking.dto.ResponseBookingForItemDto;

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
