package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.mapper.BookingRepositoryMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.StatusException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.WrongUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    @Mock
    private BookingRepositoryMapper mapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Booking booking;
    private Item item;
    private User booker;
    private User owner;
    private BookingEntity bookingEntity;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 30, 1, 1);
        LocalDateTime end = LocalDateTime.of(2024, 2, 1, 1, 1);

        booker = new User(2L, "John", "jonh@mail.ru");
        owner = new User(1L, "adam", "adam@mail.ru");

        item = new Item(1L, "Дрель", "Ударная", true, owner, null, null, null,null,null);

        booking = new Booking(1L, start, end, item, booker, BookingStatus.WAITING);

        bookingEntity = new BookingEntity();

        when(userService.getById(booker.getId())).thenReturn(booker);
        when(itemService.getById(booker.getId(), item.getId())).thenReturn(item);
        when(mapper.toBookingEntity(any(Booking.class))).thenReturn(bookingEntity);
        when(mapper.toBooking(any(BookingEntity.class))).thenReturn(booking);
    }

    @Test
    void createCorrect() {
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);

        Booking createdBooking = bookingService.create(booking);

        assertNotNull(createdBooking);
        verify(userService).getById(booker.getId());
        verify(itemService).getById(booker.getId(), item.getId());
        verify(bookingRepository).save(any(BookingEntity.class));
    }

    @Test
    void createBookingFailOwnerBooking() {
        when(userService.getById(booker.getId())).thenReturn(owner);
        when(itemService.getById(owner.getId(), item.getId())).thenReturn(item);

        assertThrows(AccessDeniedException.class, () -> bookingService.create(booking));
    }

    @Test
    void createBookingFailItemNotAvailable() {
        item.setAvailable(false);

        assertThrows(ValidationException.class, () -> bookingService.create(booking));
    }

    @Test
    void updateStatusSuccessful() {
        long userId = owner.getId();
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(bookingEntity));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);

        Booking updatedBooking = bookingService.updateStatus(userId, booking.getId(), true);

        assertNotNull(updatedBooking);
        assertEquals(BookingStatus.APPROVED, updatedBooking.getStatus());
    }

    @Test
    void updateStatusFailWrongUser() {
        long userId = 2L;
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(bookingEntity));

        assertThrows(WrongUserException.class, () -> bookingService.updateStatus(userId, booking.getId(), true));
    }

    @Test
    void updateStatus_Fail_StatusAlreadySetAPPROVED() {
        long userId = owner.getId();
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(bookingEntity));

        assertThrows(StatusException.class, () -> bookingService.updateStatus(userId, booking.getId(), true));
    }

    @Test
    void updateStatus_Fail_StatusAlreadySetREJECTED() {
        long userId = owner.getId();
        booking.setStatus(BookingStatus.REJECTED);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(bookingEntity));

        assertThrows(StatusException.class, () -> bookingService.updateStatus(userId, booking.getId(), false));
    }

    @Test
    public void getByIdSuccessful() {
        long bookingId = booking.getId();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingEntity));

        Booking foundBooking = bookingService.getById(bookingId);

        assertNotNull(foundBooking);
    }

    @Test
    public void getById_Fail_NotFound() {
        long bookingId = 99L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.getById(bookingId));
    }

    @Test
    public void getBookingByUserWhenBookerSuccess() {
        long bookingId = booking.getId();
        long bookerId = booker.getId();

        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.of(bookingEntity));

        Booking result = bookingService.getBookingByUser(bookingId, bookerId);

        assertNotNull(result);
        assertEquals(bookerId, result.getBooker().getId());
    }

    @Test
    public void getBookingByUserWhenOwnerSuccess() {
        long bookingId = booking.getId();
        long ownerId = owner.getId();

        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.of(bookingEntity));

        Booking result = bookingService.getBookingByUser(bookingId, ownerId);

        assertNotNull(result);
        assertEquals(ownerId, result.getItem().getOwner().getId());
    }

    @Test
    public void getBookingByUserWhenNeitherBookerNorOwnerThrowsAccessDeniedException() {
        long someOtherUserId = 4L;
        long bookingId = booking.getId();

        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.of(bookingEntity));

        assertThrows(AccessDeniedException.class, () -> bookingService.getBookingByUser(bookingId, someOtherUserId));
    }

    @Test
    void getUserBookingsAll() {
        long userId = 1;
        BookingState state = BookingState.ALL;
        Pageable pageable = PageRequest.of(0 / 10, 10);
        List<BookingEntity> bookingEntities = List.of(bookingEntity);
        List<Booking> bookings = List.of(booking);

        when(userService.getById(booker.getId())).thenReturn(booker);
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(userId, pageable)).thenReturn(bookingEntities);

        List<Booking> expectedBookings = bookingService.getUserBookings(userId, state, pageable);
        assertEquals(expectedBookings, bookings);
    }

    @Test
    void getUserBookingsPAST() {
        long userId = 1;
        BookingState state = BookingState.PAST;
        Pageable pageable = PageRequest.of(0 / 10, 10);
        List<BookingEntity> bookingEntities = new ArrayList<>();

        when(userService.getById(booker.getId())).thenReturn(booker);
        when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.of(2024, 2, 3, 1, 1), pageable)).thenReturn(bookingEntities);

        List<Booking> result = bookingService.getUserBookings(userId, state, pageable);

        assertEquals(bookingEntities.size(), result.size());
    }

    @Test
    void getUserBookingsFUTURE() {
        long userId = 1;
        BookingState state = BookingState.FUTURE;
        Pageable pageable = PageRequest.of(0 / 10, 10);
        List<BookingEntity> bookingEntities = new ArrayList<>();

        when(userService.getById(booker.getId())).thenReturn(booker);
        when(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.of(2024, 2, 3, 1, 1), pageable)).thenReturn(bookingEntities);

        List<Booking> result = bookingService.getUserBookings(userId, state, pageable);

        assertEquals(bookingEntities.size(), result.size());
    }

    @Test
    void getUserBookingsCURRENT() {
        long userId = 1;
        BookingState state = BookingState.CURRENT;
        Pageable pageable = PageRequest.of(0 / 10, 10);
        List<BookingEntity> bookingEntities = new ArrayList<>();

        when(userService.getById(booker.getId())).thenReturn(booker);
        when(bookingRepository.findAllByBookerIdAndCurrentTimeBetweenStartAndEnd(userId, LocalDateTime.of(2024, 2, 3, 1, 1), pageable)).thenReturn(bookingEntities);

        List<Booking> result = bookingService.getUserBookings(userId, state, pageable);

        assertEquals(bookingEntities.size(), result.size());
    }

    @Test
    void getUserBookingsWAITING() {
        long userId = 1;
        BookingState state = BookingState.WAITING;
        Pageable pageable = PageRequest.of(0 / 10, 10);
        List<BookingEntity> bookingEntities = new ArrayList<>();

        when(userService.getById(booker.getId())).thenReturn(booker);
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, pageable)).thenReturn(bookingEntities);

        List<Booking> result = bookingService.getUserBookings(userId, state, pageable);

        assertEquals(bookingEntities.size(), result.size());
    }

    @Test
    void getUserBookingsREJECTED() {
        long userId = 1;
        BookingState state = BookingState.REJECTED;
        Pageable pageable = PageRequest.of(0 / 10, 10);
        List<BookingEntity> bookingEntities = new ArrayList<>();

        when(userService.getById(booker.getId())).thenReturn(booker);
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, pageable)).thenReturn(bookingEntities);

        List<Booking> result = bookingService.getUserBookings(userId, state, pageable);

        assertEquals(bookingEntities.size(), result.size());
    }
}