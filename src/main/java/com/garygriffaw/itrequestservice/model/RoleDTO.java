package com.garygriffaw.itrequestservice.model;

import com.garygriffaw.itrequestservice.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDTO {
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}
