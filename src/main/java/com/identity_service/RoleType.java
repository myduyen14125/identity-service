package com.identity_service;

public enum RoleType {
//    2 roles: admin and user
    admin(1),
    user(2);

    public static RoleType getDefault() {
        return RoleType.user;
    }

    private final int value;

    RoleType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RoleType fromValue(int value) {
        for (RoleType role : RoleType.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role value: " + value);
    }
}
