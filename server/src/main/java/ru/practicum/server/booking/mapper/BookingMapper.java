package ru.practicum.server.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.server.booking.dto.RequestBookingDto;
import ru.practicum.server.booking.dto.ResponseBookingDto;
import ru.practicum.server.booking.dto.ResponseBookingForItemDto;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.user.mapper.UserMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = UserMapper.class)
public interface BookingMapper {
    @Mapping(target = "item.id", source = "requestBookingDto.itemId")
    @Mapping(target = "booker.id", source = "userId")
    Booking toBooking(RequestBookingDto requestBookingDto, Long userId);

    ResponseBookingDto toResponseBookingDto(Booking booking);

    @Mapping(target = "id", source = "booking.id")
    @Mapping(target = "bookerId", source = "booking.booker.id")
    ResponseBookingForItemDto toResponseBookingForItemDto(Booking booking);
}
