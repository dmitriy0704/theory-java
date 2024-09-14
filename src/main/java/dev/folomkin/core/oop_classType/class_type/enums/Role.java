package dev.folomkin.core.oop_classType.class_type.enums;

enum Role {
    GUEST("guest"), CLIENT("client"), MODERATOR("moderator"), ADMIN("admin");
    private String typeRole;

    Role(String typeRole) {
        this.typeRole = typeRole;
    }
}
