package com.omega.hogwatsartifactsonline.artifacts;

import com.omega.hogwatsartifactsonline.dto.ArtifactDto;
import com.omega.hogwatsartifactsonline.dtomapper.ArtifactDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/artifacts")
@RequiredArgsConstructor
public class ArtifactController {
    private final ArtifactService artifactService;
    private final ArtifactDtoMapper artifactDtoMapper;

    @GetMapping("{id}")
    public ArtifactDto findById(@PathVariable String id) {
        var artifact = artifactService.findById(id);
        return artifactDtoMapper.apply(artifact);
    }

    @GetMapping
    public List<ArtifactDto> findAll() {
        return artifactService.findAll()
                .stream()
                .map(artifactDtoMapper)
                .collect(Collectors.toList());
    }

    @PostMapping("/new")
    public Artifact addArtifact(@RequestBody @Valid
                                    ArtifactDto artifactDto) {
        Artifact artifact = artifactDtoMapper.apply(artifactDto);
        return artifactService.addArtifact(artifact);
    }

    @PutMapping("/{artifactId}")
    public Artifact updateArtifact(@RequestBody @Valid ArtifactDto updateRequest,
                                   @PathVariable String artifactId) {
        Artifact artifact = artifactDtoMapper.apply(updateRequest);
        return artifactService.updateArtifact(artifactId, artifact);
    }

    @DeleteMapping("/{artifactId}")
    public void deleteArtifactById(@PathVariable String artifactId) {
        artifactService.deleteArtifactById(artifactId);
    }
}
