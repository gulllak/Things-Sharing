package ru.practicum.shareit.item.mapper.comment;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.entity.CommentEntity;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentRepositoryMapper {
    Comment toComment(CommentEntity commentEntity);

    CommentEntity toCommentEntity(Comment comment);
}
