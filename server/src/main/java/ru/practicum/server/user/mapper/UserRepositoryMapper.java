package ru.practicum.server.user.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.server.user.entity.UserEntity;
import ru.practicum.server.user.dto.PatchUserDto;
import ru.practicum.server.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserRepositoryMapper {
    User toUser(UserEntity userEntity);

    UserEntity toUserEntity(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserEntity toUser(PatchUserDto patchUserDto, @MappingTarget UserEntity entity);
}
