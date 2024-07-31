package dev.folomkin.core.oop.enums;

enum Role {
    GUEST("guest"), CLIENT("client"), MODERATOR("moderator"), ADMIN("admin");
    private String typeRole;

    Role(String typeRole) {
        this.typeRole = typeRole;
    }
}
