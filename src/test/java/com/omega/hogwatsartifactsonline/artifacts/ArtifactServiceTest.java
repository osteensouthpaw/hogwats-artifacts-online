package com.omega.hogwatsartifactsonline.artifacts;

import com.omega.hogwatsartifactsonline.Exceptions.ArtifactNotFoundException;
import com.omega.hogwatsartifactsonline.wizards.Wizard;
import de.mkammerer.snowflakeid.SnowflakeIdGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {
    @Mock
    ArtifactRepository artifactRepository;
    @Mock
    SnowflakeIdGenerator idGenerator;
    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Artifact artifact1 = new Artifact();
        artifact1.setArtifactId("1");
        artifact1.setName("Invisibility Cloak");
        artifact1.setDescription("A magical cloak that renders the wearer invisible");
        artifact1.setImageUrl("imageUrl1");

        Artifact artifact2 = new Artifact();
        artifact2.setArtifactId("2");
        artifact2.setName("Marauder's Map");
        artifact2.setDescription("A magical map of Hogwarts that reveals the castle's layout and the location of individuals");
        artifact2.setImageUrl("imageUrl2");

        artifacts.add(artifact1);
        artifacts.add(artifact2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByIdSuccess() {
        //given(mock data)
        var wizard = new Wizard();
        wizard.setId("osteen");
        wizard.setName("harry");

        var artifact = new Artifact();
        artifact.setArtifactId("omega");
        artifact.setName("invisibility cloak");
        artifact.setDescription("makes the wearer of the cloak to be invisible");
        artifact.setOwner(wizard);

        given(artifactRepository.findById("omega")).willReturn(Optional.of(artifact));

        //when
        Artifact returnedArtifact = artifactService.findById("omega");

        //then
        assertThat(returnedArtifact).isEqualTo(artifact);
        verify(artifactRepository, times(1)).findById("omega");
    }

    @Test
    void findByIdUnSuccessful() {
        //given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        //when
        var throwable = catchThrowable(() -> artifactService.findById("omega"));

        //then
        assertThat(throwable)
                .isInstanceOf(ArtifactNotFoundException.class)
                .hasMessage("artifact not found");

        verify(artifactRepository, times(1)).findById("omega");
    }


    @Test
    void findAllSuccess() {
        //given
        given(artifactRepository.findAll()).willReturn(artifacts);

        //when
        List<Artifact> actual = artifactService.findAll();

        assertThat(actual.size()).isEqualTo(artifacts.size());
        verify(artifactRepository, times(1)).findAll();
    }


    @Test
    void saveArtifactSuccess() {
        //given
        Artifact artifact = new Artifact();
        artifact.setName("osteen");
        artifact.setDescription("Description...");
        artifact.setImageUrl("imageUrl...");

        given(idGenerator.next()).willReturn(123456L);
        given(artifactRepository.save(artifact)).willReturn(artifact);

        //when
        Artifact savedArtifact = artifactService.addArtifact(artifact);

        //then
        assertThat(artifact.getArtifactId()).isEqualTo(savedArtifact.getArtifactId());
        assertThat(artifact.getName()).isEqualTo(savedArtifact.getName());
        assertThat(artifact.getDescription()).isEqualTo(savedArtifact.getDescription());
        assertThat(artifact.getImageUrl()).isEqualTo(savedArtifact.getImageUrl());

        verify(artifactRepository, times(1)).save(artifact);
    }

    @Test
    void updateArtifactSuccess() {
        Artifact oldArtifact = new Artifact();
        oldArtifact.setArtifactId("1");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("A magical cloak that renders the wearer invisible");
        oldArtifact.setImageUrl("imageUrl1");

        Artifact newArtifact = new Artifact();
        newArtifact.setArtifactId("1");
        newArtifact.setName("Invisibility Cloak");
        newArtifact.setDescription("A magical cloak that renders the wearer invisible");
        newArtifact.setImageUrl("imageUrl1");

        //given
        given(artifactRepository.findById("1")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        //when
        Artifact updatedArtifact = artifactService.updateArtifact("1", newArtifact);

        //then
        assertThat(updatedArtifact.getArtifactId()).isEqualTo(oldArtifact.getArtifactId());
        assertThat(updatedArtifact.getDescription()).isEqualTo(oldArtifact.getDescription());
        verify(artifactRepository, times(1)).save(oldArtifact);
        verify(artifactRepository, times(1)).findById("1");
    }

    @Test
    void updateArtifactUnsuccessful() {
        Artifact artifact = new Artifact();
        artifact.setName("osteen");
        artifact.setDescription("Description...");
        artifact.setImageUrl("imageUrl...");

        //given
        given(artifactRepository.findById("1")).willReturn(Optional.empty());

        //when
        assertThrows(ArtifactNotFoundException.class, () -> {
            artifactService.updateArtifact("1", artifact);
        });

        //then
        verify(artifactRepository, times(1)).findById("1");
    }


    @Test
    void deleteByIdSuccessful() {
        Artifact oldArtifact = new Artifact();
        oldArtifact.setArtifactId("1");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("A magical cloak that renders the wearer invisible");
        oldArtifact.setImageUrl("imageUrl1");

        //given
        given(artifactRepository.findById("1")).willReturn(Optional.of(oldArtifact));
        doNothing().when(artifactRepository).deleteById("1");

        //when
        artifactService.deleteArtifactById("1");

        //then
        verify(artifactRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteByIdUnsuccessful() {
        given(artifactRepository.findById("1")).willReturn(Optional.empty());

        assertThrows(ArtifactNotFoundException.class, () -> {
            artifactService.deleteArtifactById("1");
        });

        verify(artifactRepository, times(1)).findById("1");
    }
}