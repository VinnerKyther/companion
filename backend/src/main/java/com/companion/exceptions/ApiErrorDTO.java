package com.companion.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
}
