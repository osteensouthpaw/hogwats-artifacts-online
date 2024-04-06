package com.omega.hogwatsartifactsonline.wizards;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration test for Wizard API endpoints")
@Tag("integration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WizardControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @Test
    void findWizardByIdSuccessful() throws Exception {
        //when and then
        mockMvc.perform(get("/api/v1/wizards/1")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wizardId").value(1))
                .andExpect(jsonPath("$.name").value("harry"));
    }


    @Test
    void findWizardByIdUnsuccessful() throws Exception {
        //when and then
        mockMvc.perform(get("/api/v1/wizards/20")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("wizard not found"));
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findAllWizards() throws Exception {
        //when and then
        mockMvc.perform(get("/api/v1/wizards")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(3)));
    }


    @Test
    void addWizardSuccessful() throws Exception {
        var savedWizard = new Wizard();
        savedWizard.setName("osteen");

        String artifactJson = objectMapper.writeValueAsString(savedWizard);

        //when and then
        mockMvc.perform(post("/api/v1/wizards/new")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(artifactJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wizardId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("osteen"));
        mockMvc.perform(get("/api/v1/wizards")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(4)));
    }


    @Test
    void updateWizardSuccessful() throws Exception {
        Wizard updatedWizard = new Wizard();
        updatedWizard.setWizardId(1);
        updatedWizard.setName("harry-potter");

        String wizardJson = objectMapper.writeValueAsString(updatedWizard);

        //when and then
        mockMvc.perform(put("/api/v1/wizards/1")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wizardJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void deleteByIdSuccessful() throws Exception {
        //when and then
        mockMvc.perform(delete("/api/v1/wizards/1")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/wizards")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)));
    }


    @Test
    void deleteByIdUnsuccessful() throws Exception {
        mockMvc.perform(delete("/api/v1/wizards/12")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("not found"));
    }
}
