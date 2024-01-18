package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.StatusException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.WrongUserException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Transactional
    @Override
    public Booking create(Booking booking) {
        booking.setBooker(userService.getById(booking.getBooker().getId()));
        booking.setItem(itemService.getById(booking.getItem().getId()));
        booking.setStatus(BookingStatus.WAITING);

        if(booking.getItem().getOwner().getId() == booking.getBooker().getId()) {
            throw new AccessDeniedException("Владелец не может забронировать свою вещь");
        }

        if(!booking.getItem().getAvailable()) {
            throw new ValidationException("Вещь недоступна для аренды");
        }

        validateBookingTime(booking.getStart(), booking.getEnd());

        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking updateStatus(long userId, Long bookingId, boolean isStatus) {
        Booking booking = getById(bookingId);
        if (booking.getItem().getOwner().getId() != userId) {
            throw new WrongUserException("Изменить статус бронирования может только владелец вещи");
        }
        if (isStatus) {
            if(booking.getStatus().equals(BookingStatus.APPROVED)) {
                throw new StatusException("Данный статус уже был установлен ранее");
            }
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            if(booking.getStatus().equals(BookingStatus.REJECTED)) {
                throw new StatusException("Данный статус уже был установлен ранее");
            }
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getById(long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new EntityNotFoundException("Бронирования с таким id не найдено"));
    }

    @Override
    public Booking getBookingByUser(Long bookingId, long userId) {
        Booking booking = getById(bookingId);

        long bookerId = booking.getBooker().getId();
        long ownerId = booking.getItem().getOwner().getId();

        if(bookerId != userId && ownerId != userId) {
            throw new AccessDeniedException("Бронирование может проверить владелец вещи или букер");
        }
        return booking;
    }

    @Override
    public List<Booking> getUserBookings(long userId, BookingState state) {
        userService.getById(userId);
        List<Booking> bookings = new ArrayList<>();

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndCurrentTimeBetweenStartAndEnd(userId, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
        }

        return bookings;
    }

    @Override
    public List<Booking> getOwnerBookings(long userId, BookingState state) {
        userService.getById(userId);
        List<Booking> bookings = new ArrayList<>();

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
                break;
            case PAST:
                bookings = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItemOwnerIdAndCurrentTimeBetweenStartAndEnd(userId, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
        }

        return bookings;
    }

    private void validateBookingTime(LocalDateTime start, LocalDateTime end) {
        if(end.isBefore(LocalDateTime.now())) {
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
