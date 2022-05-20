package com.ironhack.midtermproject.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RoleToUserDTO {
    @NotEmpty(message = "You must have a username")
    private String username;
    @NotEmpty(message = "You must have a role name")
    private String roleName;

    public RoleToUserDTO(String username, String roleName) {
        this.username = username;
        this.roleName = roleName;
    }
}
