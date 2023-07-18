package com.garygriffaw.itrequestservice.enums;

public enum RequestStatusEnum {
    CREATED("Created"),
    ASSIGNED("Assigned"),
    IN_WORK("In Work"),
    COMPLETE("Complete"),
    CANCELLED("Cancelled");

    public final String displayValue;

    RequestStatusEnum(String displayValue) {
        this.displayValue = displayValue;
    }
}
