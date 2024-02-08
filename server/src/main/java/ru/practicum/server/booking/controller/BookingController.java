package ru.practicum.server.booking.controller;

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
import ru.practicum.server.booking.dto.RequestBookingDto;
import ru.practicum.server.booking.dto.ResponseBookingDto;
import ru.practicum.server.booking.mapper.BookingMapper;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.model.BookingState;
import ru.practicum.server.booking.service.BookingService;
import ru.practicum.server.item.controller.ItemController;

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
                                               @RequestHeader(value = ItemController.X_SHARER_USER_ID) long userId) {
        return mapper.toResponseBookingDto(bookingService.getBookingByUser(bookingId, userId));
    }

    @GetMapping
    public List<ResponseBookingDto> getUserBookings(@RequestHeader(value = ItemController.X_SHARER_USER_ID) long userId,
                                                    @RequestParam(value = "state") BookingState state,
                                                    @RequestParam(value = "from") int from,
                                                    @RequestParam(value = "size") int size) {
        return bookingService.getUserBookings(userId, state, from, size).stream()
                .map(mapper::toResponseBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<ResponseBookingDto> getOwnerBookings(@RequestHeader(value = ItemController.X_SHARER_USER_ID) long userId,
                                                     @RequestParam(value = "state") BookingState state,
                                                     @RequestParam(value = "from") int from,
                                                     @RequestParam(value = "size") int size) {
        return bookingService.getOwnerBookings(userId, state, from, size).stream()
                .map(mapper::toResponseBookingDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseBookingDto create(@RequestHeader(value = ItemController.X_SHARER_USER_ID) long userId,
                                     @RequestBody RequestBookingDto requestBookingDto) {
        Booking booking = mapper.toBooking(requestBookingDto, userId);

        return mapper.toResponseBookingDto(bookingService.create(booking));
    }

    @PatchMapping("/{bookingId}")
    public ResponseBookingDto updateStatus(@RequestHeader(value = ItemController.X_SHARER_USER_ID) long userId,
                                           @PathVariable("bookingId") Long bookingId,
                                           @RequestParam(value = "approved") boolean isStatus) {
        return mapper.toResponseBookingDto(bookingService.updateStatus(userId, bookingId, isStatus));
    }
}
