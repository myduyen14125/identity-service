package com.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.identity_service.dto.request.RoleRequest;
import com.identity_service.dto.response.RoleResponse;
import com.identity_service.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true) // when mapping, ignore the setString permission
    Role toRole(RoleRequest roleRequest);

    RoleResponse toRoleResponse(Role role);
}
