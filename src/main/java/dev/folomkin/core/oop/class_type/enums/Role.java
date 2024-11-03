package dev.folomkin.core.oop.class_type.enums;

enum Role {
    GUEST("guest"), CLIENT("client"), MODERATOR("moderator"), ADMIN("admin");
    private String typeRole;

    Role(String typeRole) {
        this.typeRole = typeRole;
    }
}
