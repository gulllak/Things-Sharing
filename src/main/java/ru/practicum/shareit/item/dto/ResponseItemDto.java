package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.ResponseBookingForItemDto;

@Setter
@Getter
public class ResponseItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private ResponseBookingForItemDto lastBooking;
    private ResponseBookingForItemDto nextBooking;
}
