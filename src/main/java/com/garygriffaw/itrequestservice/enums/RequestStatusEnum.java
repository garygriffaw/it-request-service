package com.garygriffaw.itrequestservice.enums;

import java.util.Arrays;
import java.util.Optional;

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

    public static Optional<RequestStatusEnum> findByName(String name) {
        return Arrays.stream(values()).filter(status -> status.name().equalsIgnoreCase(name)).findFirst();
    }
}
