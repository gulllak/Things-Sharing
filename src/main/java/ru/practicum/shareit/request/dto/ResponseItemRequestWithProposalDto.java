package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.item.ResponseItemForRequest;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ResponseItemRequestWithProposalDto {
    private long id;
    private String description;
    private LocalDateTime created;
    private List<ResponseItemForRequest> items;
}
