package com.identity_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.identity_service.dto.request.RoleRequest;
import com.identity_service.dto.response.ApiResponse;
import com.identity_service.dto.response.RoleResponse;
import com.identity_service.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(roleRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{roleId}")
    ApiResponse<String> deleteRoleById(@PathVariable String roleId) {
        roleService.delete(roleId);
        return ApiResponse.<String>builder()
                .message("Deleted role with id " + roleId + " successfully")
                .build();
    }
}
