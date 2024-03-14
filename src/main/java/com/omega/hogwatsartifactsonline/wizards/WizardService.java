package com.omega.hogwatsartifactsonline.wizards;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WizardService {
    private final WizardRepository wizardRepository;

    public Wizard findById(String id) {
        return wizardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
    }
}
