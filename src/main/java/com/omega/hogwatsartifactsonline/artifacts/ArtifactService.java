package com.omega.hogwatsartifactsonline.artifacts;

import com.omega.hogwatsartifactsonline.Exceptions.ArtifactNotFoundException;
import de.mkammerer.snowflakeid.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtifactService {
    private final ArtifactRepository artifactRepository;
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    public Artifact findById(String id) {
        return artifactRepository.findById(id)
                .orElseThrow(() -> new ArtifactNotFoundException("artifact not found"));
    }

    public List<Artifact> findAll() {
        return artifactRepository.findAll();
    }

    public Artifact addArtifact(Artifact artifact) {
        String artifactId = String.valueOf(snowflakeIdGenerator.next());
        artifact.setArtifactId(artifactId);
        return artifactRepository.save(artifact);
    }

    public Artifact updateArtifact(String artifactId, Artifact updateRequest) {
        return artifactRepository.findById(artifactId)
                .map(artifact -> {
                    artifact.setName(updateRequest.getName());
                    artifact.setDescription(updateRequest.getDescription());
                    artifact.setImageUrl(updateRequest.getImageUrl());

                    return artifactRepository.save(artifact);
                })
                .orElseThrow(() -> new ArtifactNotFoundException("artifact not found %s".formatted(artifactId)));
    }

    public void deleteArtifactById(String artifactId) {
        artifactRepository.findById(artifactId)
                        .orElseThrow(() -> new ArtifactNotFoundException("not found"));
        artifactRepository.deleteById(artifactId);
    }
}
