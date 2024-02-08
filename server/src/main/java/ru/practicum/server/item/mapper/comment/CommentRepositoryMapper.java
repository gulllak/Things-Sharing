package ru.practicum.server.item.mapper.comment;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.server.item.entity.CommentEntity;
import ru.practicum.server.item.model.Comment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentRepositoryMapper {
    Comment toComment(CommentEntity commentEntity);

    CommentEntity toCommentEntity(Comment comment);
}
