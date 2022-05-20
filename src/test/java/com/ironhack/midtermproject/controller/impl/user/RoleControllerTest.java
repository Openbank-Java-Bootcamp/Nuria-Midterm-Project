package com.ironhack.midtermproject.controller.impl.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.DTO.RoleToUserDTO;
import com.ironhack.midtermproject.model.user.Admin;
import com.ironhack.midtermproject.model.user.Role;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.repository.user.RoleRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.service.impl.user.RoleService;
import com.ironhack.midtermproject.utils.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class RoleControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        roleRepository.saveAll(List.of(
                new Role(null, "ROLE_USER"),
                new Role(null, "ROLE_ADMIN")
        ));

        Address address = new Address("Evergreen Terrace", "Springfield", "United States", "742");
        LocalDate date = LocalDate.of(1998, 6, 25);

        User admin = new Admin(null, "Admin", "admin", "1234", new ArrayList<>());
        userRepository.save(admin);

        roleService.addRoleToUser("admin", "ADMIN");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void saveRole_Valid_Created() throws Exception {
        Role role = new Role(null,"THIRD_PARTY");
        String body = objectMapper.writeValueAsString(role);
        MvcResult mvcResult = mockMvc.perform(post("/api/roles")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void addRoleTOUser_Valid_NoContent() throws Exception {
        RoleToUserDTO roleToUserDTO = new RoleToUserDTO("admin", "HOLDER");
        String body = objectMapper.writeValueAsString(roleToUserDTO);
        MvcResult mvcResult = mockMvc.perform(post("/api/roles/addtouser")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
    }
}