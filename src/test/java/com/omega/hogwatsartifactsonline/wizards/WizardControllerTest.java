package com.omega.hogwatsartifactsonline.wizards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omega.hogwatsartifactsonline.Exceptions.ResourceNotFoundException;
import com.omega.hogwatsartifactsonline.artifacts.Artifact;
import com.omega.hogwatsartifactsonline.dto.WizardDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WizardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WizardService wizardService;

    @Autowired
    ObjectMapper objectMapper;

    List<Wizard> wizards = new ArrayList<>();

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

        Artifact artifact3 = new Artifact();
        artifact3.setArtifactId("3");
        artifact3.setName("Philosopher's Stone");
        artifact3.setDescription("Grants immortality and transforms any metal into pure gold");
        artifact3.setImageUrl("imageUrl3");

        Artifact artifact4 = new Artifact();
        artifact4.setArtifactId("4");
        artifact4.setName("Sorting Hat");
        artifact4.setDescription("Magical hat that sorts incoming students at Hogwarts into one of the four houses");
        artifact4.setImageUrl("imageUrl4");

        Artifact artifact5 = new Artifact();
        artifact5.setArtifactId("5");
        artifact5.setName("Time-Turner");
        artifact5.setDescription("Magical device that allows the wearer to travel back in time");
        artifact5.setImageUrl("imageUrl5");

        Artifact artifact6 = new Artifact();
        artifact6.setArtifactId("6");
        artifact6.setName("Elder Wand");
        artifact6.setDescription("One of the three Deathly Hallows; said to be the most powerful wand in existence");
        artifact6.setImageUrl("imageUrl6");

        var wizard = new Wizard();
        wizard.setWizardId(1);
        wizard.setName("harry");
        wizard.addArtifact(artifact1);
        wizard.addArtifact(artifact3);
        wizards.add(wizard);

        var wizard2 = new Wizard();
        wizard2.setWizardId(2);
        wizard2.setName("Neville Longbottom");
        wizard2.addArtifact(artifact2);
        wizard2.addArtifact(artifact4);
        wizards.add(wizard2);

        var wizard3 = new Wizard();
        wizard3.setWizardId(3);
        wizard3.setName("Albus");
        wizard3.addArtifact(artifact5);
        wizards.add(wizard3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findWizardByIdSuccessful() throws Exception {
        //given
        given(wizardService.findById(1)).willReturn(wizards.get(0));

        //when and then
        mockMvc.perform(get("/api/v1/wizards/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wizardId").value(1))
                .andExpect(jsonPath("$.name").value("harry"));
    }


    @Test
    void findWizardByIdUnsuccessful() throws Exception {
        //given
        given(wizardService.findById(1))
                .willThrow(new ResourceNotFoundException("not found"));

        //when and then
        mockMvc.perform(get("/api/v1/wizards/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("not found"));
    }

    @Test
    void findAllWizards() throws Exception {
        given(wizardService.findAll()).willReturn(wizards);
        //when and then
        mockMvc.perform(get("/api/v1/wizards")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(wizards.size())));
    }

    @Test
    void addWizardSuccessful() throws Exception {
        //given
        WizardDto wizardDto = new WizardDto(
                1,
                "harry",
                2
        );

        String artifactJson = objectMapper.writeValueAsString(wizardDto);

        var savedWizard = new Wizard();
        savedWizard.setWizardId(1);
        savedWizard.setName("harry");

        given(wizardService.addWizard(Mockito.any(Wizard.class))).willReturn(savedWizard);

        //when and then
        mockMvc.perform(post("/api/v1/wizards/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(artifactJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wizardId").isNotEmpty())
                .andExpect(jsonPath("$.name").value(savedWizard.getName()));
    }

    @Test
    void updateWizardSuccessful() throws Exception {
        WizardDto wizardDto = new WizardDto(
                1,
                "harry",
                2
        );

        Wizard updatedWizard = new Wizard();
        updatedWizard.setWizardId(1);
        updatedWizard.setName("harry-potter");

        String wizardJson = objectMapper.writeValueAsString(wizardDto);

        //given
        given(wizardService.updateWizard(eq(1),Mockito.any(Wizard.class))).willReturn(updatedWizard);

        //when and then
        mockMvc.perform(put("/api/v1/wizards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wizardJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wizardId").value(1))
                .andExpect(jsonPath("$.name").value(updatedWizard.getName()));
    }


    @Test
    void updateWizardWithNonExistentId() throws Exception {
        //given
        WizardDto wizardDto = new WizardDto(
                1,
                "harry",
                2
        );

        String wizardJson = objectMapper.writeValueAsString(wizardDto);

        given(wizardService.updateWizard(eq(1), Mockito.any(Wizard.class))).willReturn(null);

        //when and then
        mockMvc.perform(put("/api/v1/wizards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wizardJson)
                .accept(MediaType.APPLICATION_JSON));
    }


    @Test
    void deleteByIdSuccessful() throws Exception {
        //given
        doNothing().when(wizardService).deleteWizardById(1);
        //when and then

        mockMvc.perform(delete("/api/v1/wizards/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByIdUnsuccessful() throws Exception {
        doThrow(new ResourceNotFoundException("not found"))
                .when(wizardService)
                .deleteWizardById(1);

        mockMvc.perform(delete("/api/v1/wizards/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("not found"));
    }


    @Test
    void artifactAssignmentSuccessful() throws Exception {
        //given
        doNothing().when(wizardService)
                .artifactAssignment(2, "1");

        //when and then
        mockMvc.perform(put("/api/v1/wizards/2/artifacts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void artifactAssignmentUnsuccessfulWithNonExistentWizardId() throws Exception {
        doThrow(new ResourceNotFoundException("wizard not found"))
                .when(wizardService)
                .artifactAssignment(1, "1");

        mockMvc.perform(put("/api/v1/wizards/1/artifacts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("wizard not found"));
    }


    @Test
    void artifactAssignmentUnsuccessfulWithNonExistentArtifactId() throws Exception {
        doThrow(new ResourceNotFoundException("artifact not found"))
                .when(wizardService)
                .artifactAssignment(1, "1");

        mockMvc.perform(put("/api/v1/wizards/1/artifacts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("artifact not found"));
    }

}