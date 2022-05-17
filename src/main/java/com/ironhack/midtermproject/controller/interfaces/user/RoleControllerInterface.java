package com.ironhack.midtermproject.controller.interfaces.user;

import com.ironhack.midtermproject.DTO.RoleToUserDTO;
import com.ironhack.midtermproject.model.user.Role;

public interface RoleControllerInterface {
    void saveRole(Role role);
    void addRoleTOUser(RoleToUserDTO roleToUserDTO);
}
