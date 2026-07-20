package com.gihan.AIAuditEngine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String fName;
    private String lName;
    private String email;
    private String password;
    private String organizationName;
    private String organizationSlug;
}
