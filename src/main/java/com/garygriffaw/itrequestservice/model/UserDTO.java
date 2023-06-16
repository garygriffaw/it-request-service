package com.garygriffaw.itrequestservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Integer id;

    private String username;
    private String firstname;
    private String lastname;
    private String email;
}
