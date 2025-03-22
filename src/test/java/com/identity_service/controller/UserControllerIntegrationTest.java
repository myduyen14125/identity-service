package com.identity_service.controller;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.identity_service.dto.request.UserCreationRequest;
import com.identity_service.dto.response.UserResponse;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Testcontainers
public class UserControllerIntegrationTest {
    @Container
    static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:9.2.0");

    @DynamicPropertySource
    static void configurationDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driverClassName", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update"); // or create-drop
        registry.add("spring.jpa.show-sql", () -> true);
    }

    @Autowired
    private MockMvc mockMvc;

    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(1990, 1, 1);

        userCreationRequest = UserCreationRequest.builder()
                .username("yuuTest")
                .firstName("yuu")
                .lastName("Test")
                .password("123456")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("cf0600f538b3")
                .username("yuuTest")
                .firstName("yuu")
                .lastName("Test")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(userCreationRequest);

        // when, then (and expect)
        var res = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("result.username").value(userCreationRequest.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("result.firstName").value(userCreationRequest.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("result.lastName").value(userCreationRequest.getLastName()));
        log.info("Res: " + res.andReturn().getResponse().getContentAsString());
        // then
    }

    @Test
    void createUser_usernameInvalid_fail() throws Exception {
        // given
        userCreationRequest.setUsername("y1");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(userCreationRequest);

        // when, then (and expect)
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(7))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("message").value("Username must be between 3 to 20 characters"));
        // then
    }
}
