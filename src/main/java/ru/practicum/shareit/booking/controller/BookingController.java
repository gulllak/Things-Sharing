package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
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
import ru.practicum.shareit.item.controller.ItemController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
@Validated
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
                                                    @RequestParam(value = "state", required = false, defaultValue = "ALL") BookingState state,
                                                    @RequestParam(value = "from", required = false, defaultValue = "0")
                                                        @Min(value = 0, message = "Начало не может быть отрицательным") int from,
                                                    @RequestParam(value = "size", required = false, defaultValue = "10")
                                                        @Min(value = 1, message = "Размер должен быть больше 0") int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        return bookingService.getUserBookings(userId, state, pageable).stream()
                .map(mapper::toResponseBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<ResponseBookingDto> getOwnerBookings(@RequestHeader(value = ItemController.X_SHARER_USER_ID) long userId,
                                                     @RequestParam(value = "state", required = false, defaultValue = "ALL") BookingState state,
                                                     @RequestParam(value = "from", required = false, defaultValue = "0")
                                                         @Min(value = 0, message = "Начало не может быть отрицательным") int from,
                                                     @RequestParam(value = "size", required = false, defaultValue = "10")
                                                         @Min(value = 1, message = "Размер должен быть больше 0") int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        return bookingService.getOwnerBookings(userId, state, pageable).stream()
                .map(mapper::toResponseBookingDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseBookingDto create(@RequestHeader(value = ItemController.X_SHARER_USER_ID) long userId,
                                     @RequestBody @Valid RequestBookingDto requestBookingDto) {
        return mapper.toResponseBookingDto(bookingService.create(mapper.toBooking(requestBookingDto, userId)));
    }

    @PatchMapping("/{bookingId}")
    public ResponseBookingDto updateStatus(@RequestHeader(value = ItemController.X_SHARER_USER_ID) long userId,
                                           @PathVariable("bookingId") Long bookingId,
                                           @RequestParam(value = "approved", required = true) boolean isStatus) {
        return mapper.toResponseBookingDto(bookingService.updateStatus(userId, bookingId, isStatus));
    }

}
