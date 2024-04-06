package com.omega.hogwatsartifactsonline.hogwatsuser;


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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration test for User API endpoints")
@Tag("integration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}")
    String baseUrl;


    @Test
    void canFindUserById() throws Exception {
        mockMvc.perform(get(baseUrl + "/users/1")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("omega"))
                .andExpect(jsonPath("$.enabled").value(false))
                .andExpect(jsonPath("$.roles").value("admin"));
    }


    @Test
    void findByIdUnsuccessful() throws Exception {
        mockMvc.perform(get(baseUrl + "/users/12")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("user not found"));
    }


    @Test
    void canFindAllUsers() throws Exception {
        mockMvc.perform(get(baseUrl + "/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_admin")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(3)));
    }


    @Test
    void addUserSuccessful() throws Exception {
        //given
        HogwartsUser user = new HogwartsUser(
                null,
                "osteen",
                "1234",
                true,
                "user");

        String artifactJson = objectMapper.writeValueAsString(user);

        //when and then
        mockMvc.perform(post(  baseUrl + "/users/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_admin")))
                        .content(artifactJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").isNotEmpty())
                .andExpect(jsonPath("$.username").value("osteen"))
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.roles").value("user"));
        mockMvc.perform(get(baseUrl + "/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_admin")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(4)));
    }


    @Test
    void updateUserSuccessful() throws Exception {
        HogwartsUser updatedUser = new HogwartsUser();
        updatedUser.setUsername("omega");
        updatedUser.setEnabled(true);
        updatedUser.setRoles("admin");

        String userJson = objectMapper.writeValueAsString(updatedUser);

        //when and then
        mockMvc.perform(put(baseUrl + "/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_admin")))
                        .content(userJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("omega"))
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.roles").value("admin"));
    }


    @Test
    void deleteUserByIdSuccessful() throws Exception {
        //when and then
        mockMvc.perform(delete(baseUrl + "/users/2")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_admin")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get(baseUrl + "/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_admin")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)));
    }


    @Test
    void deleteUserByIdUnsuccessful() throws Exception {
        mockMvc.perform(delete(baseUrl + "/users/12")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_admin")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("user not found"));
    }
}
