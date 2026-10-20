package com.companion.dtos.user;

import com.companion.enums.UserRole;
import com.companion.enums.UserStatus;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String registrationNumber,
        String course,
        Integer semester,
        String bio,
        UserStatus status,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
