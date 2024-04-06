package com.omega.hogwatsartifactsonline.artifacts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omega.hogwatsartifactsonline.Exceptions.ResourceNotFoundException;
import com.omega.hogwatsartifactsonline.dto.ArtifactDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    ArtifactService artifactService;
    @Autowired
    ObjectMapper objectMapper;
    List<Artifact> artifacts;
    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        var artifact = new Artifact();
        artifact.setArtifactId("omega");
        artifact.setName("invisibility cloak");
        artifact.setDescription("makes the wearer of the cloak to be invisible");
        artifact.setImageUrl("image");
        artifacts = List.of(artifact);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByIdSuccess() throws Exception {
        //given
        given(artifactService.findById("omega")).willReturn((artifacts.get(0)));
        //when and then
        mockMvc.perform(get(  baseUrl + "/artifacts/omega")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artifactId").value("omega"))
                .andExpect(jsonPath("$.name").value("invisibility cloak"))
                .andExpect(jsonPath("$.description").value("makes the wearer of the cloak to be invisible"))
                .andExpect(jsonPath("$.imageUrl").value("image"));
    }

    @Test
    void findByIdUnSuccessful() throws Exception {
        //given
        given(artifactService.findById("omega"))
                .willThrow(new ResourceNotFoundException("not found"));

        //when and then
        mockMvc.perform(get(  baseUrl + "/artifacts/omega")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("not found"));
    }

    @Test
    void findAllSuccessful() throws Exception {
        //given
        given(artifactService.findAll()).willReturn(artifacts);
        //when and then
        mockMvc.perform(get(baseUrl + "/artifacts")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(artifacts.size())));
    }


    @Test
    void addArtifactSuccess() throws Exception {
        //given
        ArtifactDto artifactDto = new ArtifactDto(
                null,
                "invisibility cloak",
                "makes the wearer of the cloak to be invisible",
                "image",
                null
        );

        String artifactJson = objectMapper.writeValueAsString(artifactDto);

        var savedArtifact = new Artifact();
        savedArtifact.setArtifactId("omega");
        savedArtifact.setName("invisibility cloak");
        savedArtifact.setDescription("makes the wearer of the cloak to be invisible");
        savedArtifact.setImageUrl("image");

        given(artifactService.addArtifact(Mockito.any(Artifact.class))).willReturn(savedArtifact);

        //when and then
        mockMvc.perform(post(  baseUrl + "/artifacts/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(artifactJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artifactId").isNotEmpty())
                .andExpect(jsonPath("$.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.imageUrl").value(savedArtifact.getImageUrl()));
    }


    @Test
    void updateArtifactSuccess() throws Exception {
        ArtifactDto artifactDto = new ArtifactDto(
                "1",
                "invisibility cloak",
                "makes the wearer of the cloak to be invisible",
                "image",
                null
        );

        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setArtifactId("1");
        updatedArtifact.setName("osteen");
        updatedArtifact.setDescription("Description...");
        updatedArtifact.setImageUrl("imageUrl...");

        String artifactJson = objectMapper.writeValueAsString(artifactDto);

        //given
        given(artifactService.updateArtifact(eq("1"),Mockito.any(Artifact.class))).willReturn(updatedArtifact);

        //when and then
        mockMvc.perform(put(baseUrl + "/artifacts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(artifactJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artifactId").value("1"))
                .andExpect(jsonPath("$.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.description").value(updatedArtifact.getDescription()))
                .andExpect(jsonPath("$.imageUrl").value(updatedArtifact.getImageUrl()));
    }


    @Test
    void deleteByIdSuccessful() throws Exception {
        //given
        doNothing().when(artifactService).deleteArtifactById("1");
        //when and then

        mockMvc.perform(delete(baseUrl + "/artifacts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByIdUnsuccessful() throws Exception {
        doThrow(new ResourceNotFoundException("not found"))
                .when(artifactService)
                .deleteArtifactById("1");

        mockMvc.perform(delete(baseUrl + "/artifacts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("not found"));
    }
}