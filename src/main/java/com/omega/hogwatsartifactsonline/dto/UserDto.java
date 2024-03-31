package com.omega.hogwatsartifactsonline.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(
        Integer userId,
        @NotEmpty(message = "username cannot be empty")
        String username,
        boolean enabled,
        @NotEmpty(message = "role cannot be empty")
        String roles
) {
}
