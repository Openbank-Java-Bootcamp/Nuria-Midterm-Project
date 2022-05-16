package com.ironhack.midtermproject.service.interfaces;

import com.ironhack.midtermproject.model.user.Role;

public interface RoleServiceInterface {
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
}
