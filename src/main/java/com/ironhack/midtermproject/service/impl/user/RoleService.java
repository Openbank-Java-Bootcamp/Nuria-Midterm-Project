package com.ironhack.midtermproject.service.impl.user;

import com.ironhack.midtermproject.model.user.Role;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.repository.user.RoleRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.service.interfaces.user.RoleServiceInterface;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService implements RoleServiceInterface {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    public Role saveRole(Role role) {
        log.info("Saving a new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role to user {}", username);
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
        userRepository.save(user);
    }
}
