package com.gigigenie.domain.member.enums;

public enum MemberRole {
    GUEST("guest"),
    USER("member"),
    ADMIN("admin");

    private final String value;

    MemberRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MemberRole fromValue(String value) {
        for (MemberRole role : MemberRole.values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}