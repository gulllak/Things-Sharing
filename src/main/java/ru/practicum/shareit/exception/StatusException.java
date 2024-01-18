package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatusException extends RuntimeException {
    public StatusException(String message) {
        super(message);
        log.error(message);
    }
}
