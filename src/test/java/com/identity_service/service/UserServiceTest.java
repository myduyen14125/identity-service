package com.identity_service.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.identity_service.dto.request.UserCreationRequest;
import com.identity_service.dto.response.UserResponse;
import com.identity_service.entity.User;
import com.identity_service.exception.AppException;
import com.identity_service.repository.UserRepository;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
    private User user;
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

        user = User.builder()
                .id("cf0600f538b3")
                .username("yuuTest")
                .password("123456")
                .firstName("yuu")
                .lastName("Test")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        // given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);
        // when
        var result = userService.createUser(userCreationRequest);
        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUsername()).isEqualTo(userCreationRequest.getUsername());
        Assertions.assertThat(result.getFirstName()).isEqualTo(userCreationRequest.getFirstName());
    }

    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(userCreationRequest));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(3);
    }

    @Test
    @WithMockUser(username = "yuuTest")
    void getMyInfo_validRequest_success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        var response = userService.getMyInfo();
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUsername()).isEqualTo(userCreationRequest.getUsername());
        Assertions.assertThat(response.getFirstName()).isEqualTo(userCreationRequest.getFirstName());
    }

    @Test
    @WithMockUser(username = "yuuTest")
    void getMyInfo_userNotFound_error() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(2);
    }
}
