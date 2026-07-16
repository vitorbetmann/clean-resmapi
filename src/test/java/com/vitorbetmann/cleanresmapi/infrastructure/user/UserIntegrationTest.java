package com.vitorbetmann.cleanresmapi.infrastructure.user;


import com.vitorbetmann.cleanresmapi.TestcontainersConfiguration;
import com.vitorbetmann.cleanresmapi.infrastructure.user.dtos.CreateUserRequest;
import com.vitorbetmann.cleanresmapi.infrastructure.user.dtos.UpdateUserRequest;
import com.vitorbetmann.cleanresmapi.infrastructure.usertype.dtos.CreateUserTypeRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import tools.jackson.databind.json.JsonMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Transactional
public class UserIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JsonMapper jsonMapper;

    String mockName = "John";
    String mockEmail = "john@fiap.com";
    String mockPassword = "password123";
    String mockAddress = "123 John St";
    Long userTypeId;
    Long mockId = 999999L;

    @BeforeEach
    void setUp() throws Exception {
        userTypeId = createUserType("Owner");
    }

    @Test
    void create_whenEmailIsUnique_returnsCreated() throws Exception {
        var request = new CreateUserRequest(mockName, mockEmail, mockPassword, userTypeId, mockAddress);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(mockName))
                .andExpect(jsonPath("$.email").value(mockEmail))
                .andExpect(jsonPath("$.userTypeId").value(userTypeId))
                .andExpect(jsonPath("$.address").value(mockAddress))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void create_whenEmailIsDuplicate_returnsConflict() throws Exception {
        var request = new CreateUserRequest(mockName, mockEmail, mockPassword, userTypeId, mockAddress);
        var json = jsonMapper.writeValueAsString(request);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        var duplicateRequest = new CreateUserRequest("Jane", mockEmail, mockPassword, userTypeId, mockAddress);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("User email already in use: " + mockEmail));
    }

    @Test
    void create_whenNameIsBlank_returnsBadRequest() throws Exception {
        var request = new CreateUserRequest("", mockEmail, mockPassword, userTypeId, mockAddress);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_whenUserTypeIdDoesNotExist_returnsNotFound() throws Exception {
        var request = new CreateUserRequest(mockName, mockEmail, mockPassword, mockId, mockAddress);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("UserType ID not found: " + mockId));
    }

    @Test
    void get_whenIdExists_returnsUser() throws Exception {
        var id = createUser(mockName, mockEmail, mockPassword, userTypeId, mockAddress);

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value(mockEmail));
    }

    @Test
    void get_whenIdDoesNotExist_returnsNotFound() throws Exception {
        mockMvc.perform(get("/users/{id}", mockId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("User ID not found: " + mockId));
    }

    @Test
    void getAll_returnsAllUsers() throws Exception {
        createUser(mockName, mockEmail, mockPassword, userTypeId, mockAddress);
        createUser("Jane", "jane@fiap.com", mockPassword, userTypeId, mockAddress);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void update_whenIdExists_returnsUpdatedUser() throws Exception {
        var id = createUser(mockName, mockEmail, mockPassword, userTypeId, mockAddress);
        var updatedName = "Updated Name";
        var request = new UpdateUserRequest(updatedName, mockEmail, mockPassword, userTypeId, mockAddress);

        mockMvc.perform(put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedName));
    }

    @Test
    void update_whenIdDoesNotExist_returnsNotFound() throws Exception {
        var request = new UpdateUserRequest(mockName, mockEmail, mockPassword, userTypeId, mockAddress);

        mockMvc.perform(put("/users/{id}", mockId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("User ID not found: " + mockId));
    }

    @Test
    void update_whenEmailBelongsToAnotherUser_returnsConflict() throws Exception {
        createUser(mockName, mockEmail, mockPassword, userTypeId, mockAddress);
        var otherId = createUser("Jane", "jane@fiap.com", mockPassword, userTypeId, mockAddress);

        var request = new UpdateUserRequest("Jane", mockEmail, mockPassword, userTypeId, mockAddress);

        mockMvc.perform(put("/users/{id}", otherId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("User email already in use: " + mockEmail));
    }

    @Test
    void delete_whenIdExists_returnsNoContent() throws Exception {
        var id = createUser(mockName, mockEmail, mockPassword, userTypeId, mockAddress);

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_whenIdDoesNotExist_returnsNotFound() throws Exception {
        mockMvc.perform(delete("/users/{id}", mockId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("User ID not found: " + mockId));
    }

    private Long createUserType(String name) throws Exception {
        var request = new CreateUserTypeRequest(name);
        var json = mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return jsonMapper.readTree(json).get("id").asLong();
    }

    private Long createUser(String name, String email, String password, Long userTypeId, String address) throws Exception {
        var request = new CreateUserRequest(name, email, password, userTypeId, address);
        var json = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return jsonMapper.readTree(json).get("id").asLong();
    }
}
