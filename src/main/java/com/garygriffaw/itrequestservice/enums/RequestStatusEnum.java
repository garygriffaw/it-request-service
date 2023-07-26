package com.garygriffaw.itrequestservice.enums;

import java.util.Arrays;
import java.util.Optional;

public enum RequestStatusEnum {
    CREATED("Created", false, false),
    ASSIGNED("Assigned", false, true),
    IN_WORK("In Work", false, true),
    COMPLETE("Complete", false, true),
    CANCELLED("Cancelled", true, true);

    public final String displayValue;
    public final boolean isValidRequestStatusForRequester;
    public final boolean isValidRequestStatusForAssignedTo;

    RequestStatusEnum(String displayValue, boolean isValidRequestStatusForRequester, boolean isValidRequestStatusForAssignedTo) {
        this.displayValue = displayValue;
        this.isValidRequestStatusForRequester = isValidRequestStatusForRequester;
        this.isValidRequestStatusForAssignedTo = isValidRequestStatusForAssignedTo;
    }

    public static Optional<RequestStatusEnum> findByName(String name) {
        return Arrays.stream(values()).filter(status -> status.name().equalsIgnoreCase(name)).findFirst();
    }
}
