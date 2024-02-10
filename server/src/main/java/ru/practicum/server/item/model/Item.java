package ru.practicum.server.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.user.model.User;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;
    private Booking lastBooking;
    private Booking nextBooking;
    List<Comment> comments;
    private Long requestId;
}
