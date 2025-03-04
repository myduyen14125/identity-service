package com.identity_service.mapper;

import com.identity_service.RoleType;
import com.identity_service.dto.request.UserCreationRequest;
import com.identity_service.dto.request.UserUpdateRequest;
import com.identity_service.dto.response.UserResponse;
import com.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "role", target = "role", qualifiedByName = "mapRole")
    User toUser(UserCreationRequest request);

    @Named("mapRole")
    default RoleType mapRole(Integer role) {
        return role != null ? RoleType.fromValue(role) : null;
    }

    @Mapping(source = "role", target = "role", qualifiedByName = "mapRole")
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    UserResponse toUserResponse(User user);
}
