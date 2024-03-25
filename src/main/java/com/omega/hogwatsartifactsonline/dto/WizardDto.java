package com.omega.hogwatsartifactsonline.dto;

import jakarta.validation.constraints.NotEmpty;

public record WizardDto(
        int wizardId,
        @NotEmpty(message = "name cannot be empty")
        String name,
        int numberOfArtifacts
) {
}
