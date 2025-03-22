package com.identity_service.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String password; // no need to return it in future
    String firstName;
    String lastName;
    String email;
    Integer phone;
    List<RoleResponse> roles;
    LocalDate dob;
}
