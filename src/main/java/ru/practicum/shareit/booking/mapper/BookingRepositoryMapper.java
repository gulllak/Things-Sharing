package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = UserRepositoryMapper.class)

public interface BookingRepositoryMapper {
    BookingEntity toBookingEntity(Booking booking);

    Booking toBooking(BookingEntity bookingEntity);
}
