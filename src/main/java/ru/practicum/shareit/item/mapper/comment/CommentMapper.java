package ru.practicum.shareit.item.mapper.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.comment.RequestCommentDto;
import ru.practicum.shareit.item.dto.comment.ResponseCommentDto;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    @Mapping(target = "item.id", source = "itemId")
    @Mapping(target = "author.id", source = "userId")
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    Comment toComment(RequestCommentDto requestCommentDto, Long itemId, Long userId);

    @Mapping(target = "authorName", source = "comment.author.name")
    ResponseCommentDto toResponseComment(Comment comment);
}
