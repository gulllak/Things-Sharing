package ru.practicum.server.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WrongUserException extends RuntimeException {
    public WrongUserException(String message) {
        super(message);
        log.error(message);
    }
}
