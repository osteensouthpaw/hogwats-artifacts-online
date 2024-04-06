package com.omega.hogwatsartifactsonline.hogwatsuser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omega.hogwatsartifactsonline.Exceptions.ResourceNotFoundException;
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
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    List<HogwartsUser> users = new ArrayList<>();

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        HogwartsUser user = new HogwartsUser(
                1,
                "osteenomega",
                "1234",
                true,
                "user");
        users.add(user);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAllUsersSuccessful() throws Exception {
        //given
        given(userService.findAll()).willReturn(users);

        //when and then
        mockMvc.perform(get(baseUrl + "/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(users.size())));
    }

    @Test
    void findUserByIdSuccessful() throws Exception {
        //given
        given(userService.findById(1)).willReturn(users.get(0));

        //when and then
        mockMvc.perform(get(baseUrl + "/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("osteenomega"))
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.roles").value("user"));
    }

    @Test
    void findByIdUnSuccessful() throws Exception {
        //given
        given(userService.findById(1))
                .willThrow(new ResourceNotFoundException("user not found"));

        //when and then
        mockMvc.perform(get(baseUrl + "/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("user not found"));
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

        HogwartsUser savedUser = new HogwartsUser(
                1,
                "osteen",
                "1234",
                true,
                "user");

        given(userService.save(Mockito.any(HogwartsUser.class))).willReturn(savedUser);

        //when and then
        mockMvc.perform(post(  baseUrl + "/users/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(artifactJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").isNotEmpty())
                .andExpect(jsonPath("$.username").value(savedUser.getUsername()))
                .andExpect(jsonPath("$.enabled").value(savedUser.isEnabled()))
                .andExpect(jsonPath("$.roles").value(savedUser.getRoles()));
    }


    @Test
    void updateUserSuccessful() throws Exception {
        HogwartsUser user = new HogwartsUser(
                1,
                "osteen",
                "1234",
                true,
                "user");

        HogwartsUser updatedUser = new HogwartsUser();
        updatedUser.setUserId(1);
        updatedUser.setUsername("omega");
        updatedUser.setEnabled(true);
        updatedUser.setRoles("admin");

        String userJson = objectMapper.writeValueAsString(user);

        //given
        given(userService.update(eq(1),Mockito.any(HogwartsUser.class))).willReturn(updatedUser);

        //when and then
        mockMvc.perform(put(baseUrl + "/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value(updatedUser.getUsername()))
                .andExpect(jsonPath("$.enabled").value(updatedUser.isEnabled()))
                .andExpect(jsonPath("$.roles").value(updatedUser.getRoles()));
    }


    @Test
    void deleteUserByIdSuccessful() throws Exception {
        //given
        doNothing().when(userService).delete(1);
        //when and then

        mockMvc.perform(delete(baseUrl + "/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserByIdUnsuccessful() throws Exception {
        doThrow(new ResourceNotFoundException("user not found"))
                .when(userService)
                .delete(1);

        mockMvc.perform(delete(baseUrl + "/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("user not found"));
    }
}