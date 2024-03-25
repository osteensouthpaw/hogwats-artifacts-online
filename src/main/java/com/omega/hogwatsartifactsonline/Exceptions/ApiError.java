package com.omega.hogwatsartifactsonline.Exceptions;

import java.time.LocalDateTime;

public record ApiError(
        Object message,
        String path,
        int statusCode,
        LocalDateTime localDateTime
) {
}
