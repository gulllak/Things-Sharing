package ru.practicum.server.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.server.user.dto.RequestUserDto;
import ru.practicum.server.user.dto.ResponseUserDto;
import ru.practicum.server.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toUser(RequestUserDto requestUserDto);

    ResponseUserDto toResponseDto(User user);
}
