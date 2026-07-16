package com.vitorbetmann.cleanresmapi.infrastructure.menuitem;


import com.vitorbetmann.cleanresmapi.TestcontainersConfiguration;
import com.vitorbetmann.cleanresmapi.infrastructure.menuitem.dtos.CreateMenuItemRequest;
import com.vitorbetmann.cleanresmapi.infrastructure.menuitem.dtos.UpdateMenuItemRequest;
import com.vitorbetmann.cleanresmapi.infrastructure.restaurant.dtos.CreateRestaurantRequest;
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

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Transactional
public class MenuItemIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JsonMapper jsonMapper;

    String mockName = "Spaghetti Carbonara";
    String mockDescription = "Classic Roman pasta with egg, cheese, and pancetta.";
    BigDecimal mockPrice = new BigDecimal("28.90");
    String mockCategory = "Main Course";
    Long mockId = 999999L;

    Long restaurantId;

    @BeforeEach
    void setUp() throws Exception {
        var userTypeId = createUserType("Owner");
        var ownerId = createUser("John", "john@fiap.com", "password123", userTypeId, "123 John St");
        restaurantId = createRestaurant("Cantina da Praça", "123 Main St", "Italian", "09:00-22:00", ownerId);
    }

    @Test
    void create_whenFieldsAreValid_returnsCreated() throws Exception {
        var request = new CreateMenuItemRequest(mockName, mockDescription, mockPrice, mockCategory, restaurantId);

        mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(mockName))
                .andExpect(jsonPath("$.description").value(mockDescription))
                .andExpect(jsonPath("$.price").value(mockPrice.doubleValue()))
                .andExpect(jsonPath("$.category").value(mockCategory))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId));
    }

    @Test
    void create_whenNameIsBlank_returnsBadRequest() throws Exception {
        var request = new CreateMenuItemRequest("", mockDescription, mockPrice, mockCategory, restaurantId);

        mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_whenPriceIsZeroOrLess_returnsBadRequest() throws Exception {
        var request = new CreateMenuItemRequest(mockName, mockDescription, BigDecimal.ZERO, mockCategory, restaurantId);

        mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_whenRestaurantIdDoesNotExist_returnsNotFound() throws Exception {
        var request = new CreateMenuItemRequest(mockName, mockDescription, mockPrice, mockCategory, mockId);

        mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Restaurant ID not found: " + mockId));
    }

    @Test
    void get_whenIdExists_returnsMenuItem() throws Exception {
        var id = createMenuItem(mockName, mockDescription, mockPrice, mockCategory, restaurantId);

        mockMvc.perform(get("/menu-items/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(mockName));
    }

    @Test
    void get_whenIdDoesNotExist_returnsNotFound() throws Exception {
        mockMvc.perform(get("/menu-items/{id}", mockId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("MenuItem ID not found: " + mockId));
    }

    @Test
    void getAll_returnsAllMenuItems() throws Exception {
        createMenuItem(mockName, mockDescription, mockPrice, mockCategory, restaurantId);
        createMenuItem("Tiramisu", "Classic Italian dessert.", new BigDecimal("15.00"), "Dessert", restaurantId);

        mockMvc.perform(get("/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void update_whenIdExists_returnsUpdatedMenuItem() throws Exception {
        var id = createMenuItem(mockName, mockDescription, mockPrice, mockCategory, restaurantId);
        var updatedName = "Spaghetti Bolognese";
        var request = new UpdateMenuItemRequest(updatedName, mockDescription, mockPrice, mockCategory, restaurantId);

        mockMvc.perform(put("/menu-items/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedName));
    }

    @Test
    void update_whenIdDoesNotExist_returnsNotFound() throws Exception {
        var request = new UpdateMenuItemRequest(mockName, mockDescription, mockPrice, mockCategory, restaurantId);

        mockMvc.perform(put("/menu-items/{id}", mockId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("MenuItem ID not found: " + mockId));
    }

    @Test
    void delete_whenIdExists_returnsNoContent() throws Exception {
        var id = createMenuItem(mockName, mockDescription, mockPrice, mockCategory, restaurantId);

        mockMvc.perform(delete("/menu-items/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/menu-items/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_whenIdDoesNotExist_returnsNotFound() throws Exception {
        mockMvc.perform(delete("/menu-items/{id}", mockId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("MenuItem ID not found: " + mockId));
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

    private Long createMenuItem(String name, String description, BigDecimal price, String category, Long restaurantId) throws Exception {
        var request = new CreateMenuItemRequest(name, description, price, category, restaurantId);
        var json = mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return jsonMapper.readTree(json).get("id").asLong();
    }
}
