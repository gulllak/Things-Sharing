package ru.practicum.server.booking.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseBookingForItemDto {
    private long id;
    private long bookerId;
}
