package com.omega.hogwatsartifactsonline.dtomapper;

import com.omega.hogwatsartifactsonline.artifacts.Artifact;
import com.omega.hogwatsartifactsonline.dto.ArtifactDto;
import com.omega.hogwatsartifactsonline.dto.WizardDto;
import com.omega.hogwatsartifactsonline.wizards.Wizard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ArtifactDtoMapper implements Function<Artifact, ArtifactDto> {
    private final WizardDtoMapper wizardDtoMapper;

    @Override
    public ArtifactDto apply(Artifact artifact) {
        WizardDto owner = (artifact.getOwner() != null) ? wizardDtoMapper.apply(artifact.getOwner()) : null;

        return new ArtifactDto(
                artifact.getArtifactId(),
                artifact.getName(),
                artifact.getDescription(),
                artifact.getImageUrl(),
                owner
        );
    }

    public Artifact apply(ArtifactDto artifactDto) {
        var artifact = new Artifact();
        artifact.setName(artifactDto.name());
        artifact.setDescription(artifactDto.description());
        artifact.setImageUrl(artifactDto.imageUrl());

        return artifact;
    }
}
