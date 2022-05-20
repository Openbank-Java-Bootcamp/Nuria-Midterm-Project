package com.ironhack.midtermproject;

import com.ironhack.midtermproject.model.user.Admin;
import com.ironhack.midtermproject.model.user.Role;
import com.ironhack.midtermproject.service.impl.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class MidtermProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MidtermProjectApplication.class, args);
    }

    @Autowired
    private Environment environment;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService, RoleService roleService) {
        return args -> {
            if (!Arrays.asList(environment.getActiveProfiles()).contains("test")) {
                roleService.saveRole(new Role(null, "ADMIN"));
                roleService.saveRole(new Role(null, "HOLDER"));

                userService.saveUser(new Admin(null, "John Doe", "john", "1234", new ArrayList<>()));

                roleService.addRoleToUser("john", "ADMIN");
            }
        };
    }
}
