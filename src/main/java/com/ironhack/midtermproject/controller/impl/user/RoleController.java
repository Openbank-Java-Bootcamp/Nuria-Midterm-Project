package com.ironhack.midtermproject.controller.impl.user;

import com.ironhack.midtermproject.DTO.RoleToUserDTO;
import com.ironhack.midtermproject.controller.interfaces.user.RoleControllerInterface;
import com.ironhack.midtermproject.model.user.Role;
import com.ironhack.midtermproject.service.interfaces.user.RoleServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RoleController implements RoleControllerInterface {
    @Autowired
    private RoleServiceInterface roleService;

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveRole(@RequestBody @Valid Role role) {
        roleService.saveRole(role);
    }

    @PostMapping("/roles/addtouser")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addRoleTOUser(@RequestBody @Valid RoleToUserDTO roleToUserDTO) {
        roleService.addRoleToUser(roleToUserDTO.getUsername(), roleToUserDTO.getRoleName());
    }
}
