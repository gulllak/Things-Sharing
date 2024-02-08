package ru.practicum.server.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
        log.error(message);
    }
}
