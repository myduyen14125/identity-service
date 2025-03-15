package com.identity_service.dto.response;

import com.identity_service.enums.RoleType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
