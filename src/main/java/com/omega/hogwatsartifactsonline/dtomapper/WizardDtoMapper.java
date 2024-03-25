package com.omega.hogwatsartifactsonline.dtomapper;

import com.omega.hogwatsartifactsonline.dto.WizardDto;
import com.omega.hogwatsartifactsonline.wizards.Wizard;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class WizardDtoMapper implements Function<Wizard, WizardDto> {
    @Override
    public WizardDto apply(Wizard wizard) {
        return new WizardDto(
                wizard.getWizardId(),
                wizard.getName(),
                wizard.getNumberOfArtifacts()
        );
    }

    public Wizard apply(WizardDto wizardDto) {
        var wizard = new Wizard();
        wizard.setName(wizardDto.name());
        wizard.setWizardId(wizardDto.wizardId());

        return wizard;
    }
}
