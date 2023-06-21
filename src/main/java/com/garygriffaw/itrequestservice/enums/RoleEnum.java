package com.garygriffaw.itrequestservice.enums;

public enum RoleEnum {
    USER,
    ADMIN;

    public String roleName() {
        return "ROLE_" + this;
    }
}
