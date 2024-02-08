package ru.practicum.gateway.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.gateway.exception.ValidationException;
import ru.practicum.gateway.booking.dto.BookingState;
import ru.practicum.gateway.booking.dto.RequestBookingDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBookingByUser(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") String stringState,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.exist(stringState).orElseThrow(() -> new ValidationException("Unknown state: " + stringState));

        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @RequestParam(name = "state", defaultValue = "ALL") String stringState,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.exist(stringState).orElseThrow(() -> new ValidationException("Unknown state: " + stringState));

        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getOwnerBookings(userId, state, from, size);
    }


    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid RequestBookingDto requestBookingDto) {
        validateBookingTime(requestBookingDto.getStart(), requestBookingDto.getEnd());

        log.info("Creating booking {}, userId={}", requestBookingDto, userId);
        return bookingClient.create(userId, requestBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @PathVariable("bookingId") long bookingId,
                                               @RequestParam(value = "approved") boolean isStatus) {
        log.info("Update booking {} status userId={}", bookingId, userId);
        return bookingClient.updateStatus(userId, bookingId, String.valueOf(isStatus));
    }

    private void validateBookingTime(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время завершения бронирования раньше текущей даты");
        } else if (end.isBefore(start)) {
            throw new ValidationException("Время завершения бронирования раньше начала");
        } else if (start.isEqual(end)) {
            throw new ValidationException("Время завершения бронирования равно времени начала");
        } else if (start.isAfter(end)) {
            throw new ValidationException("Время начала бронирования позже завершения");
        } else if (start.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время начала бронирования раньше текущей даты");
        }
    }
}
