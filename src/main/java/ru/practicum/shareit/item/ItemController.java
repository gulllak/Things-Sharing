package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ItemDto create(@RequestHeader(value = "X-Sharer-User-Id") long id,
                          @RequestBody @Valid ItemDto itemDto) {
        Item item = itemDtoToItem(itemDto);
        item.setOwner(id);

        return itemToItemDto(itemService.create(item));
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                           @PathVariable("itemId") long itemId) {
        return itemToItemDto(itemService.getById(itemId));
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemService.getAll(userId).stream()
                .map(this::itemToItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> itemSearch(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                    @RequestParam("text") String searchString) {
        return itemService.itemSearch(userId, searchString).stream()
                .map(this::itemToItemDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable("itemId") long itemId,
                          @RequestHeader(value = "X-Sharer-User-Id") long userId,
                          @RequestBody ItemDto itemDto) {
        Item item = itemDtoToItem(itemDto);
        item.setId(itemId);
        item.setOwner(userId);
        return itemToItemDto(itemService.update(item));
    }


    private Item itemDtoToItem(ItemDto itemDto) {
        return modelMapper.map(itemDto, Item.class);
    }

    private ItemDto itemToItemDto(Item item) {
        return modelMapper.map(item, ItemDto.class);
    }
}
