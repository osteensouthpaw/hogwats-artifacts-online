package com.omega.hogwatsartifactsonline.wizards;

import com.omega.hogwatsartifactsonline.artifacts.Artifact;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void findByIdSuccess() {
        //given
        Artifact artifact = new Artifact();
        artifact.setArtifactId("2");
        artifact.setName("invisibility cloak");
        artifact.setDescription("makes the wearer invisible");

        Wizard wizard = new Wizard();
        wizard.setId("1");
        wizard.setName("");

        artifact.setOwner(wizard);
        //when
        //then
    }
}