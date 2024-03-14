package com.omega.hogwatsartifactsonline.dto;

import jakarta.validation.constraints.NotEmpty;

public record ArtifactDto(
        String artifactId,
        @NotEmpty(message = "name cannot be empty")
        String name,
        @NotEmpty(message = "description cannot be empty")
        String description,
        @NotEmpty(message = "image cannot be empty")
        String imageUrl,
        WizardDto owner
) {
}
