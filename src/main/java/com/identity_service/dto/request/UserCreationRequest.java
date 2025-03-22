package com.identity_service.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import com.identity_service.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, max = 20, message = "INVALID_USERNAME")
    private String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    private String password;

    private String firstName;
    private String lastName;

    @Email(message = "INVALID_EMAIL")
    private String email;

    private Integer phone;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    private LocalDate dob;
}
