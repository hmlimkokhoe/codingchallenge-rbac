package com.globalside.codingchallenge.rbac.web;

import com.globalside.codingchallenge.rbac.model.UserRole;
import com.globalside.codingchallenge.rbac.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @WithMockUser(roles = UserRole.ADMIN)
    void adminRoleHasCrudAccessTest() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/products/1")
                        .contentType("application/json")
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = UserRole.USER)
    void userRoleHasReadOnlyAccessTest() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getProductsForbiddenForInvalidCredentialsTest() throws Exception {
        mockMvc.perform(get("/products")
                        .with(httpBasic("guest", "somePassword")))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/products")
                .with(httpBasic("user", "adminPassword")))
                .andExpect(status().isUnauthorized());
    }
}
