package ru.practicum.server.booking.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.server.booking.model.BookingStatus;
import ru.practicum.server.item.dto.item.ResponseItemDto;
import ru.practicum.server.user.dto.ResponseUserDto;

import java.time.LocalDateTime;

@Setter
@Getter
public class ResponseBookingDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private ResponseUserDto booker;
    private ResponseItemDto item;
}
