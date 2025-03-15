package com.identity_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, max = 20, message = "INVALID_USERNAMES")
    private String username;
    @Size(min = 6, message = "INVALID_PASSWORD")
    private String password;
    private String firstName;
    private String lastName;
    @Email(message = "INVALID_EMAIL")
    private String email;
    private Integer phone;
    private LocalDate dob;
}
