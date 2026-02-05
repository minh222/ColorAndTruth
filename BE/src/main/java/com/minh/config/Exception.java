package com.minh.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class Exception {
    public static ResponseStatusException http(int code, String message) {
        return new ResponseStatusException(
                HttpStatus.valueOf(code),
                message);
    }

    public static void throwEx(int statusCode, String message) {
        throw new ResponseStatusException(
                HttpStatus.valueOf(statusCode),
                message);
    }
}
