package ru.practicum.server.booking.service;

import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    Booking create(Booking booking);

    Booking updateStatus(long userId, Long bookingId, boolean isStatus);

    Booking getById(long bookingId);

    Booking getBookingByUser(long bookingId, long userId);

    List<Booking> getUserBookings(long userId, BookingState state, int from, int size);

    List<Booking> getOwnerBookings(long userId, BookingState state, int from, int size);
}
