package ru.practicum.gateway.booking.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class RequestBookingDto {
    @NotNull(message = "Должен передавать id вещи")
    @Min(value = 0, message = "id вещи не может быть 0 или меньше")
    private Long itemId;
    @NotNull(message = "Время начала аренды не может быть null")
    private LocalDateTime start;
    @NotNull(message = "Время окончания аренды не может быть null")
    private LocalDateTime end;
}
