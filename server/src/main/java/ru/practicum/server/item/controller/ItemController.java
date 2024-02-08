package ru.practicum.server.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.server.item.dto.item.PatchItemDto;
import ru.practicum.server.item.mapper.comment.CommentMapper;
import ru.practicum.server.item.model.Comment;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.service.ItemService;
import ru.practicum.server.item.dto.comment.RequestCommentDto;
import ru.practicum.server.item.dto.item.RequestItemDto;
import ru.practicum.server.item.dto.comment.ResponseCommentDto;
import ru.practicum.server.item.dto.item.ResponseItemDto;
import ru.practicum.server.item.mapper.item.ItemMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @PostMapping
    public ResponseItemDto create(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                  @RequestBody RequestItemDto postItemDto) {
        Item item = itemMapper.toItem(postItemDto, userId);

        return itemMapper.toResponseDto(itemService.create(item));
    }

    @GetMapping("/{itemId}")
    public ResponseItemDto getById(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                   @PathVariable("itemId") long itemId) {
        return itemMapper.toResponseDto(itemService.getById(userId, itemId));
    }

    @GetMapping
    public List<ResponseItemDto> getAllUserItems(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                                 @RequestParam(value = "from") int from,
                                                 @RequestParam(value = "size") int size) {
        return itemService.getAllUserItems(userId, from, size).stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ResponseItemDto> itemSearch(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                            @RequestParam("text") String searchString,
                                            @RequestParam(value = "from") int from,
                                            @RequestParam(value = "size") int size) {
        return itemService.itemSearch(userId, searchString, from, size).stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{itemId}")
    public ResponseItemDto update(@PathVariable("itemId") long itemId,
                                  @RequestHeader(value = X_SHARER_USER_ID) long userId,
                                  @RequestBody PatchItemDto patchItemDto) {
        patchItemDto.setId(itemId);

        Item item = itemMapper.toItem(patchItemDto, userId);

        return itemMapper.toResponseDto(itemService.update(item));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseCommentDto createComment(@PathVariable("itemId") long itemId,
                                            @RequestHeader(value = X_SHARER_USER_ID) long userId,
                                            @RequestBody RequestCommentDto requestCommentDto) {
        Comment comment = commentMapper.toComment(requestCommentDto, itemId, userId);

        return commentMapper.toResponseComment(itemService.createComment(comment));
    }
}
