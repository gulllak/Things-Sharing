package ru.practicum.gateway.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PatchUserDto {
    private long id;
    private String name;
    private String email;
}
