package com.garygriffaw.itrequestservice.entities;

import com.garygriffaw.itrequestservice.enums.RequestStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RequestStatus {
    @Id
    @GeneratedValue
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RequestStatusEnum requestStatusCode;

    private String requestStatusDisplay;
}
