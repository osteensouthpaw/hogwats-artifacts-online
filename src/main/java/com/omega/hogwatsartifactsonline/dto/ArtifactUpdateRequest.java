package com.omega.hogwatsartifactsonline.dto;

public record ArtifactUpdateRequest(
        String name,
        String description,
        String imageUrl
) {
}
