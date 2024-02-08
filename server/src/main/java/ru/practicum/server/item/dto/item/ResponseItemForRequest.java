package ru.practicum.server.item.dto.item;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseItemForRequest {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
