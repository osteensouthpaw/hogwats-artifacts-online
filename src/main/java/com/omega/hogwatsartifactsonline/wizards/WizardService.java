package com.omega.hogwatsartifactsonline.wizards;

import com.omega.hogwatsartifactsonline.Exceptions.ResourceNotFoundException;
import com.omega.hogwatsartifactsonline.artifacts.Artifact;
import com.omega.hogwatsartifactsonline.artifacts.ArtifactRepository;
import com.omega.hogwatsartifactsonline.artifacts.ArtifactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WizardService {
    private final WizardRepository wizardRepository;
    private final ArtifactService artifactService;
    private final ArtifactRepository artifactRepository;

    public Wizard findById(int id) {
        return wizardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("wizard not found"));
    }

    public List<Wizard> findAll() {
        return wizardRepository.findAll();
    }

    public Wizard updateWizard(int id, Wizard updateRequest) {
        return wizardRepository.findById(id)
                .map(wizard -> {
                    wizard.setName(updateRequest.getName());
                    return wizardRepository.save(wizard);
                })
                .orElseThrow(() -> new ResourceNotFoundException("wizard not found"));
    }

    public Wizard addWizard(Wizard wizard) {
        return wizardRepository.save(wizard);
    }

    public void deleteWizardById(int id) {
        Wizard wizardToBeDeleted = wizardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));
        wizardToBeDeleted.unAssignArtifacts();
        wizardRepository.deleteById(id);
    }

    public Wizard artifactAssignment(int wizardId, String artifactId) {
        Wizard wizard = findById(wizardId);
        Artifact artifact = artifactService.findById(artifactId);

        wizard.addArtifact(artifact);
        return wizardRepository.save(wizard);
    }
}
