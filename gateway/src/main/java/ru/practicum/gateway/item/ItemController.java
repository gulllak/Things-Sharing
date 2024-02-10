package ru.practicum.gateway.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.gateway.exception.ValidationException;
import ru.practicum.gateway.item.dto.comment.RequestCommentDto;
import ru.practicum.gateway.item.dto.item.PatchItemDto;
import ru.practicum.gateway.item.dto.item.RequestItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid RequestItemDto postItemDto) {
        return itemClient.create(userId, postItemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable("itemId") long itemId) {
        return itemClient.getById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemClient.getAllUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> itemSearch(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam("text") String searchString,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemClient.itemSearch(userId, searchString, from, size);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable("itemId") long itemId,
                          @RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody PatchItemDto patchItemDto) {
        validatePatchItemDto(patchItemDto);

        return itemClient.update(itemId, userId, patchItemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable("itemId") long itemId,
                                            @RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestBody @Valid RequestCommentDto requestCommentDto) {
        return itemClient.createComment(itemId, userId, requestCommentDto);
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
