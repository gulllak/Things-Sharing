package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingMapper mapper;
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public ResponseBookingDto getBookingByUser(@PathVariable("bookingId") Long bookingId,
                                               @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return mapper.toResponseBookingDto(bookingService.getBookingByUser(bookingId, userId));
    }

    @GetMapping
    public List<ResponseBookingDto> getUserBookings(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                    @RequestParam(value = "state", required = false, defaultValue = "ALL") BookingState state) {
        return bookingService.getUserBookings(userId, state).stream()
                .map(mapper::toResponseBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<ResponseBookingDto> getOwnerBookings(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                    @RequestParam(value = "state", required = false, defaultValue = "ALL") BookingState state) {
        return bookingService.getOwnerBookings(userId, state).stream()
                .map(mapper::toResponseBookingDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseBookingDto create(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                     @RequestBody @Valid RequestBookingDto requestBookingDto) {
        return mapper.toResponseBookingDto(bookingService.create(mapper.toBooking(requestBookingDto, userId)));
    }

    @PatchMapping("/{bookingId}")
    public ResponseBookingDto updateStatus(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                           @PathVariable("bookingId") Long bookingId,
                                           @RequestParam(value = "approved", required = true) boolean isStatus) {
        return mapper.toResponseBookingDto(bookingService.updateStatus(userId, bookingId, isStatus));
    }

}
