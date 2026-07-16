package com.vitorbetmann.cleanresmapi.infrastructure.usertype;


import com.vitorbetmann.cleanresmapi.TestcontainersConfiguration;
import com.vitorbetmann.cleanresmapi.infrastructure.usertype.dtos.CreateUserTypeRequest;
import com.vitorbetmann.cleanresmapi.infrastructure.usertype.dtos.UpdateUserTypeRequest;
import jakarta.transaction.Transactional;
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
public class UserTypeIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JsonMapper jsonMapper;

    String mockName = "Owner";
    Long mockId = 999999L;

    @Test
    void create_whenNameIsUnique_returnsCreated() throws Exception {
        var request = new CreateUserTypeRequest(mockName);

        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(mockName));
    }

    @Test
    void create_whenNameIsDuplicate_returnsConflict() throws Exception {
        var request = new CreateUserTypeRequest(mockName);
        var json = jsonMapper.writeValueAsString(request);

        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("UserType name already exists: " + mockName));
    }

    @Test
    void create_whenNameIsBlank_returnsBadRequest() throws Exception {
        var request = new CreateUserTypeRequest("");

        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_whenIdExists_returnsUserType() throws Exception {
        var id = createUserType(mockName);

        mockMvc.perform(get("/user-types/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(mockName));
    }

    @Test
    void get_whenIdDoesNotExist_returnsNotFound() throws Exception {
        mockMvc.perform(get("/user-types/{id}", mockId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("UserType ID not found: " + mockId));
    }

    @Test
    void getAll_returnsAllUserTypes() throws Exception {
        createUserType("Owner");
        createUserType("Customer");

        mockMvc.perform(get("/user-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void update_whenIdExists_returnsUpdatedUserType() throws Exception {
        var id = createUserType(mockName);
        var updatedName = "Updated Owner";
        var request = new UpdateUserTypeRequest(updatedName);

        mockMvc.perform(put("/user-types/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedName));
    }

    @Test
    void update_whenIdDoesNotExist_returnsNotFound() throws Exception {
        var request = new UpdateUserTypeRequest(mockName);

        mockMvc.perform(put("/user-types/{id}", mockId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("UserType ID not found: " + mockId));
    }

    @Test
    void delete_whenIdExists_returnsNoContent() throws Exception {
        var id = createUserType(mockName);

        mockMvc.perform(delete("/user-types/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/user-types/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_whenIdDoesNotExist_returnsNotFound() throws Exception {
        mockMvc.perform(delete("/user-types/{id}", mockId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("UserType ID not found: " + mockId));
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
}
