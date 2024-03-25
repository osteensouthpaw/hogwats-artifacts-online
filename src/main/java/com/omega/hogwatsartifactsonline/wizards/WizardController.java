package com.omega.hogwatsartifactsonline.wizards;

import com.omega.hogwatsartifactsonline.dto.WizardDto;
import com.omega.hogwatsartifactsonline.dtomapper.WizardDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
@RequiredArgsConstructor
public class WizardController {
    private final WizardService wizardService;
    private final WizardDtoMapper wizardDtoMapper;

    @GetMapping("{id}")
    public WizardDto findWizardById(@PathVariable int id) {
        var wizard = wizardService.findById(id);
        return wizardDtoMapper.apply(wizard);
    }

    @GetMapping
    public List<WizardDto> findWizards() {
        return wizardService.findAll()
                .stream()
                .map(wizardDtoMapper)
                .collect(Collectors.toList());
    }

    @PostMapping("/new")
    public Wizard addWizard(    @RequestBody
                                @Valid WizardDto wizardDto) {
        Wizard wizard = wizardDtoMapper.apply(wizardDto);
        return wizardService.addWizard(wizard);
    }

    @PutMapping("/{wizardId}")
    public Wizard updateWizard(@RequestBody
                               @Valid Wizard wizard,
                               @PathVariable int wizardId) {
        return wizardService.updateWizard(wizardId, wizard);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable int id) {
        wizardService.deleteWizardById(id);
    }
}
