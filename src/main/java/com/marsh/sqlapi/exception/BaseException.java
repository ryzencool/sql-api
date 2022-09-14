package com.marsh.sqlapi.exception;

import lombok.Getter;

public class BaseException extends RuntimeException {

    @Getter
    private final String code;

    @Getter
    private final String message;

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
