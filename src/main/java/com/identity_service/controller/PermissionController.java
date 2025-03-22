package com.identity_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.identity_service.dto.request.PermissionRequest;
import com.identity_service.dto.response.ApiResponse;
import com.identity_service.dto.response.PermissionResponse;
import com.identity_service.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(permissionRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<String> deletePermissionById(@PathVariable String permission) {
        permissionService.delete(permission);
        return ApiResponse.<String>builder()
                .message("Deleted permission with id " + permission + " successfully")
                .build();
    }
}
