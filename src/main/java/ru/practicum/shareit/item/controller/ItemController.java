package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.PatchItemDto;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseCommentDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.mapper.comment.CommentMapper;
import ru.practicum.shareit.item.mapper.item.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @PostMapping
    public ResponseItemDto create(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                  @RequestBody @Valid RequestItemDto postItemDto) {
        return itemMapper.toResponseDto(itemService.create(itemMapper.toItem(postItemDto, userId)));
    }

    @GetMapping("/{itemId}")
    public ResponseItemDto getById(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                           @PathVariable("itemId") long itemId) {
        return itemMapper.toResponseDto(itemService.getById(userId, itemId));
    }

    @GetMapping
    public List<ResponseItemDto> getAllUserItems(@RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemService.getAllUserItems(userId).stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ResponseItemDto> itemSearch(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                    @RequestParam("text") String searchString) {
        return itemService.itemSearch(userId, searchString).stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{itemId}")
    public ResponseItemDto update(@PathVariable("itemId") long itemId,
                          @RequestHeader(value = "X-Sharer-User-Id") long userId,
                          @RequestBody PatchItemDto patchItemDto) {
        validatePatchItemDto(patchItemDto);
        patchItemDto.setId(itemId);

        return itemMapper.toResponseDto(itemService.update(itemMapper.toItem(patchItemDto, userId)));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseCommentDto createComment(@PathVariable("itemId") long itemId,
                                            @RequestHeader(value = "X-Sharer-User-Id") long userId,
                                            @RequestBody @Valid RequestCommentDto requestCommentDto) {
        return commentMapper.toResponseComment(itemService.createComment(commentMapper.toComment(requestCommentDto, itemId, userId)));
    }


    private void validatePatchItemDto(PatchItemDto patchItemDto) {
        if (patchItemDto.getName() != null && (patchItemDto.getName().isEmpty() || patchItemDto.getName().isBlank())) {
            throw new ValidationException("Имя вещи не может быть пустым");
        }
        if (patchItemDto.getDescription() != null && (patchItemDto.getDescription().isEmpty() || patchItemDto.getDescription().isBlank())) {
            throw new ValidationException("Описание вещи не может быть пустым");
        }
    }
}
