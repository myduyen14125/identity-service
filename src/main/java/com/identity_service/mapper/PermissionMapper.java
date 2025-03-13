package com.identity_service.mapper;

import com.identity_service.dto.request.PermissionRequest;
import com.identity_service.dto.request.UserCreationRequest;
import com.identity_service.dto.request.UserUpdateRequest;
import com.identity_service.dto.response.PermissionResponse;
import com.identity_service.dto.response.UserResponse;
import com.identity_service.entity.Permission;
import com.identity_service.entity.User;
import com.identity_service.enums.RoleType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
}
