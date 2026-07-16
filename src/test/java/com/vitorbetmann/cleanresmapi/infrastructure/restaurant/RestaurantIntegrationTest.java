package com.vitorbetmann.cleanresmapi.infrastructure.restaurant;


import com.vitorbetmann.cleanresmapi.TestcontainersConfiguration;
import com.vitorbetmann.cleanresmapi.infrastructure.restaurant.dtos.CreateRestaurantRequest;
import com.vitorbetmann.cleanresmapi.infrastructure.restaurant.dtos.UpdateRestaurantRequest;
import com.vitorbetmann.cleanresmapi.infrastructure.user.dtos.CreateUserRequest;
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
public class RestaurantIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JsonMapper jsonMapper;

    String mockName = "Cantina da Praça";
    String mockAddress = "123 Main St";
    String mockCuisineType = "Italian";
    String mockOpeningHours = "09:00-22:00";
    Long mockId = 999999L;

    Long ownerUserTypeId;
    Long ownerId;

    @BeforeEach
    void setUp() throws Exception {
        ownerUserTypeId = createUserType("Owner");
        ownerId = createUser("John", "john@fiap.com", "password123", ownerUserTypeId, "123 John St");
    }

    @Test
    void create_whenFieldsAreValid_returnsCreated() throws Exception {
        var request = new CreateRestaurantRequest(mockName, mockAddress, mockCuisineType, mockOpeningHours, ownerId);

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(mockName))
                .andExpect(jsonPath("$.address").value(mockAddress))
                .andExpect(jsonPath("$.cuisineType").value(mockCuisineType))
                .andExpect(jsonPath("$.openingHours").value(mockOpeningHours))
                .andExpect(jsonPath("$.ownerId").value(ownerId));
    }

    @Test
    void create_whenNameIsBlank_returnsBadRequest() throws Exception {
        var request = new CreateRestaurantRequest("", mockAddress, mockCuisineType, mockOpeningHours, ownerId);

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_whenOwnerIdDoesNotExist_returnsNotFound() throws Exception {
        var request = new CreateRestaurantRequest(mockName, mockAddress, mockCuisineType, mockOpeningHours, mockId);

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("User ID not found: " + mockId));
    }

    @Test
    void create_whenOwnerIsNotOfTypeOwner_returnsBadRequest() throws Exception {
        var customerUserTypeId = createUserType("Customer");
        var customerId = createUser("Jane", "jane@fiap.com", "password123", customerUserTypeId, "123 Jane St");

        var request = new CreateRestaurantRequest(mockName, mockAddress, mockCuisineType, mockOpeningHours, customerId);

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Owner must be a User of type Owner."));
    }

    @Test
    void get_whenIdExists_returnsRestaurant() throws Exception {
        var id = createRestaurant(mockName, mockAddress, mockCuisineType, mockOpeningHours, ownerId);

        mockMvc.perform(get("/restaurants/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(mockName));
    }

    @Test
    void get_whenIdDoesNotExist_returnsNotFound() throws Exception {
        mockMvc.perform(get("/restaurants/{id}", mockId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Restaurant ID not found: " + mockId));
    }

    @Test
    void getAll_returnsAllRestaurants() throws Exception {
        createRestaurant(mockName, mockAddress, mockCuisineType, mockOpeningHours, ownerId);
        createRestaurant("Cantina Nova", mockAddress, mockCuisineType, mockOpeningHours, ownerId);

        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void update_whenIdExists_returnsUpdatedRestaurant() throws Exception {
        var id = createRestaurant(mockName, mockAddress, mockCuisineType, mockOpeningHours, ownerId);
        var updatedName = "Cantina Renovada";
        var request = new UpdateRestaurantRequest(updatedName, mockAddress, mockCuisineType, mockOpeningHours, ownerId);

        mockMvc.perform(put("/restaurants/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedName));
    }

    @Test
    void update_whenIdDoesNotExist_returnsNotFound() throws Exception {
        var request = new UpdateRestaurantRequest(mockName, mockAddress, mockCuisineType, mockOpeningHours, ownerId);

        mockMvc.perform(put("/restaurants/{id}", mockId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Restaurant ID not found: " + mockId));
    }

    @Test
    void delete_whenIdExists_returnsNoContent() throws Exception {
        var id = createRestaurant(mockName, mockAddress, mockCuisineType, mockOpeningHours, ownerId);

        mockMvc.perform(delete("/restaurants/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/restaurants/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_whenIdDoesNotExist_returnsNotFound() throws Exception {
        mockMvc.perform(delete("/restaurants/{id}", mockId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Restaurant ID not found: " + mockId));
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

    private Long createRestaurant(String name, String address, String cuisineType, String openingHours, Long ownerId) throws Exception {
        var request = new CreateRestaurantRequest(name, address, cuisineType, openingHours, ownerId);
        var json = mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return jsonMapper.readTree(json).get("id").asLong();
    }
}
