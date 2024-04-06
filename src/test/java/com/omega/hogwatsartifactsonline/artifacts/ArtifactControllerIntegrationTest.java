package com.omega.hogwatsartifactsonline.artifacts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration test for Artifact API endpoints")
@Tag("integration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ArtifactControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @Test
    void findByIdSuccess() throws Exception {
        mockMvc.perform(get(baseUrl + "/artifacts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artifactId").value("1"))
                .andExpect(jsonPath("$.name").value("Invisibility Cloak"))
                .andExpect(jsonPath("$.description").value("A magical cloak that renders the wearer invisible"))
                .andExpect(jsonPath("$.imageUrl").value("imageUrl1"));
    }

    @Test
    void findByIdUnsuccessful() throws Exception {
        mockMvc.perform(get(  baseUrl + "/artifacts/omega")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("artifact not found"));
    }

    @Test
    void canFindAllArtifacts() throws Exception {
        mockMvc.perform(get(baseUrl + "/artifacts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(6)));
    }

    @Test
    void canAddArtifact() throws Exception {
        var savedArtifact = new Artifact();
        savedArtifact.setName("invisibility cloak");
        savedArtifact.setDescription("makes the wearer of the cloak to be invisible");
        savedArtifact.setImageUrl("image");

        String artifactJson = objectMapper.writeValueAsString(savedArtifact);

        //when and then
        mockMvc.perform(post(  baseUrl + "/artifacts/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt())
                        .content(artifactJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artifactId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("invisibility cloak"))
                .andExpect(jsonPath("$.description").value("makes the wearer of the cloak to be invisible"))
                .andExpect(jsonPath("$.imageUrl").value("image"));
        mockMvc.perform(get(baseUrl + "/artifacts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(7)));
    }


    @Test
    void updateArtifactSuccess() throws Exception {
        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setName("osteen");
        updatedArtifact.setDescription("Description...");
        updatedArtifact.setImageUrl("imageUrl...");

        String artifactJson = objectMapper.writeValueAsString(updatedArtifact);

        mockMvc.perform(put(baseUrl + "/artifacts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt())
                        .content(artifactJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void deleteArtifactSuccessful() throws Exception {
        mockMvc.perform(delete(baseUrl + "/artifacts/1")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get(baseUrl + "/artifacts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(5)));
    }


    @Test
    void deleteByIdUnsuccessful() throws Exception {
        mockMvc.perform(delete(baseUrl + "/artifacts/12")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("not found"));
    }
}
