package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.mapper.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.item.ResponseItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BookingController.class)
@Import(BookingMapperImpl.class)
class BookingControllerTest {
    @MockBean
    BookingService bookingService;

    @MockBean
    BookingMapper mapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    Booking booking;

    ResponseBookingDto responseBookingDto;

    RequestBookingDto requestBookingDto;

    User user;

    Item item;

    @BeforeEach
    void setUp() {
        requestBookingDto = new RequestBookingDto();
        requestBookingDto.setItemId(1L);
        requestBookingDto.setStart(LocalDateTime.MIN);
        requestBookingDto.setEnd(LocalDateTime.MAX);

        user = new User(1, "Jonh", "Jonh@mail.ru");
        item = new Item(1L, "Дрель", "Стробящая", true, user, null, null, null, null, null);
        booking = new Booking(1L, LocalDateTime.MIN, LocalDateTime.MAX, item, user, BookingStatus.WAITING);

        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setId(1);
        responseUserDto.setName("Jonh");
        responseUserDto.setEmail("Jonh@mail.ru");

        ResponseItemDto responseItemDto = new ResponseItemDto();
        responseItemDto.setId(1L);
        responseItemDto.setName("Дрель");
        responseItemDto.setDescription("Стробящая");
        responseItemDto.setAvailable(true);

        responseBookingDto = new ResponseBookingDto();
        responseBookingDto.setId(1);
        responseBookingDto.setStart(LocalDateTime.MIN);
        responseBookingDto.setEnd(LocalDateTime.MAX);
        responseBookingDto.setStatus(BookingStatus.WAITING);
        responseBookingDto.setBooker(responseUserDto);
        responseBookingDto.setItem(responseItemDto);
    }

    @Test
    void getBookingByUser() throws Exception {
        long bookingId = 1;
        long userId = 1;

        when(bookingService.getBookingByUser(any(Long.class), any(Long.class))).thenReturn(booking);
        when(mapper.toResponseBookingDto(any(Booking.class))).thenReturn(responseBookingDto);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                .header("X-SHARER-USER-ID", userId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(responseBookingDto)));
        verify(bookingService, times(1)).getBookingByUser(bookingId, userId);
    }

    @Test
    void getUserBookings() throws Exception {
        long userId = 1;
        int from = 0;
        int size = 10;
        BookingState bookingState = BookingState.ALL;
        Pageable pageable = PageRequest.of(from / size, size);

        List<ResponseBookingDto> responseBookingDtos = List.of(responseBookingDto);
        List<Booking> bookings = List.of(booking);

        when(bookingService.getUserBookings(any(Long.class), any(BookingState.class), any(Pageable.class))).thenReturn(bookings);
        when(mapper.toResponseBookingDto(any(Booking.class))).thenReturn(responseBookingDto);

        mvc.perform(get("/bookings")
                .header("X-SHARER-USER-ID", userId)
                        .param("state", String.valueOf(bookingState))
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(responseBookingDtos)))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].status", is(BookingStatus.WAITING.toString())))
                .andExpect(jsonPath("$[0].booker.id", is(1)))
                .andExpect(jsonPath("$[0].item.id", is(1)));
        verify(bookingService, times(1)).getUserBookings(userId, bookingState, pageable);
    }

    @Test
    void getUserNotExistBookings() throws Exception {
        long userId = 1;
        int from = 0;
        int size = 10;
        BookingState bookingState = BookingState.ALL;
        Pageable pageable = PageRequest.of(from / size, size);

        when(bookingService.getUserBookings(any(Long.class), any(BookingState.class), any(Pageable.class))).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/bookings")
                        .header("X-SHARER-USER-ID", userId)
                        .param("state", String.valueOf(bookingState))
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().is4xxClientError());
        verify(bookingService, times(1)).getUserBookings(userId, bookingState, pageable);
    }

    @Test
    void getOwnerBookings() throws Exception {
        long userId = 1;
        int from = 0;
        int size = 10;
        BookingState bookingState = BookingState.ALL;
        Pageable pageable = PageRequest.of(from / size, size);

        List<ResponseBookingDto> responseBookingDtos = List.of(responseBookingDto);
        List<Booking> bookings = List.of(booking);

        when(bookingService.getOwnerBookings(any(Long.class), any(BookingState.class), any(Pageable.class))).thenReturn(bookings);
        when(mapper.toResponseBookingDto(any(Booking.class))).thenReturn(responseBookingDto);

        mvc.perform(get("/bookings/owner")
                        .header("X-SHARER-USER-ID", userId)
                        .param("state", String.valueOf(bookingState))
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(responseBookingDtos)))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].status", is(BookingStatus.WAITING.toString())))
                .andExpect(jsonPath("$[0].booker.id", is(1)))
                .andExpect(jsonPath("$[0].item.id", is(1)));
        verify(bookingService, times(1)).getOwnerBookings(userId, bookingState, pageable);
    }

    @Test
    void createCorrect() throws Exception {
        long userId = 1;

        when(mapper.toBooking(any(RequestBookingDto.class), any(Long.class))).thenReturn(booking);
        when(bookingService.create(any(Booking.class))).thenReturn(booking);
        when(mapper.toResponseBookingDto(any(Booking.class))).thenReturn(responseBookingDto);

        mvc.perform(post("/bookings")
                .header("X-SHARER-USER-ID", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBookingDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(responseBookingDto)))
                .andExpect(jsonPath("$.id", is(1)));
        verify(bookingService, times(1)).create(booking);
    }

    @Test
    void createWithout_X_Sharer_User_Id() throws Exception {
        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBookingDto)))
                .andExpect(status().is4xxClientError());

        when(mapper.toBooking(any(RequestBookingDto.class), any(Long.class))).thenReturn(booking);
        when(bookingService.create(any(Booking.class))).thenReturn(booking);
        when(mapper.toResponseBookingDto(any(Booking.class))).thenReturn(responseBookingDto);

        verify(bookingService, times(0)).create(booking);
    }

    @Test
    void updateStatus() throws Exception {
        long bookingId = 1;
        long userId = 1;
        BookingStatus bookingStatus = BookingStatus.APPROVED;
        responseBookingDto.setStatus(bookingStatus);
        booking.setStatus(bookingStatus);

        when(bookingService.updateStatus(any(Long.class), any(Long.class), any(Boolean.class))).thenReturn(booking);
        when(mapper.toResponseBookingDto(any(Booking.class))).thenReturn(responseBookingDto);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                .header("X-SHARER-USER-ID", userId)
                .param("approved", String.valueOf(true)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(responseBookingDto)))
                .andExpect(jsonPath("$.status", is(bookingStatus.toString())));
    }
}