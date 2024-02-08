package ru.practicum.server.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.server.booking.entity.BookingEntity;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.user.mapper.UserRepositoryMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = UserRepositoryMapper.class)

public interface BookingRepositoryMapper {
    BookingEntity toBookingEntity(Booking booking);

    Booking toBooking(BookingEntity bookingEntity);
}
