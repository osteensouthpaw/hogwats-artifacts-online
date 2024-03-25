package com.omega.hogwatsartifactsonline.wizards;

import com.omega.hogwatsartifactsonline.Exceptions.ResourceNotFoundException;
import com.omega.hogwatsartifactsonline.artifacts.Artifact;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {
    @Mock
    WizardRepository wizardRepository;
    @InjectMocks
    WizardService wizardService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findWizardByIdSuccess() {
        Artifact artifact1 = new Artifact();
        artifact1.setArtifactId("1");
        artifact1.setName("Invisibility Cloak");
        artifact1.setDescription("A magical cloak that renders the wearer invisible");
        artifact1.setImageUrl("imageUrl1");

        var wizard = new Wizard();
        wizard.setWizardId(1);
        wizard.setName("harry");
        wizard.addArtifact(artifact1);

        //given
        given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));

        //when
        Wizard returnedWizard = wizardService.findById(1);
        assertThat(wizard).isEqualTo(returnedWizard);

        //then
        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void findWizardByIdUnsuccessful() {
        //given
        given(wizardRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());
        //when

        var throwable = catchThrowable(() -> wizardService.findById(1));

        assertThat(throwable)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("wizard not found");

        //then
        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void findAllWizardsSuccessful() {
        wizardRepository.findAll();
        verify(wizardRepository, times(1)).findAll();
    }


    @Test
    void addWizardSuccessful() {
        var wizard = new Wizard();
        wizard.setWizardId(1);
        wizard.setName("harry");

        //given
        given(wizardRepository.save(wizard)).willReturn(wizard);
        //when
        Wizard savedWizard = wizardService.addWizard(wizard);

        //then
        assertThat(savedWizard.getWizardId()).isEqualTo(wizard.getWizardId());
        assertThat(savedWizard.getName()).isEqualTo(wizard.getName());
        assertThat(savedWizard.getNumberOfArtifacts()).isEqualTo(wizard.getNumberOfArtifacts());

        verify(wizardRepository, times(1)).save(wizard);
    }


    @Test
    void updateWizardSuccess() {
        var wizard = new Wizard();
        wizard.setWizardId(1);
        wizard.setName("harry");

        var wizardUpdate = new Wizard();
        wizardUpdate.setWizardId(1);
        wizardUpdate.setName("harry-potter");

        //given
        given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));
        given(wizardRepository.save(wizard)).willReturn(wizard);
        //when

        Wizard updatedWizard = wizardService.updateWizard(1, wizardUpdate);

        //then
        assertThat(updatedWizard.getWizardId()).isEqualTo(wizard.getWizardId());
        assertThat(updatedWizard.getName()).isEqualTo(wizardUpdate.getName());

        verify(wizardRepository, times(1)).findById(1);
        verify(wizardRepository, times(1)).save(updatedWizard);
    }

    @Test
    void updateWizardUnsuccessful() {
        var wizard = new Wizard();
        wizard.setWizardId(1);
        wizard.setName("harry");

        //given
        given(wizardRepository.findById(1)).willReturn(Optional.empty());

        //when
        assertThrows(ResourceNotFoundException.class, () -> {
            wizardService.updateWizard(1, wizard);
        });

        //then
        verify(wizardRepository, times(1)).findById(1);
    }


    @Test
    void deleteWizardByIdSuccessful() {
        //given
        var wizard = new Wizard();
        wizard.setWizardId(1);
        wizard.setName("harry");

        given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));
        doNothing().when(wizardRepository).deleteById(1);

        //when
        wizardService.deleteWizardById(1);
        //then
        verify(wizardRepository, times(1)).deleteById(1);
    }
}