package com.ironhack.midtermproject.controller.impl.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.model.user.Admin;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.repository.user.AdminRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class AdminControllerTest {
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
    private AdminRepository adminRepository;
    Address address;
    LocalDate date;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        address = new Address("Evergreen Terrace", "Springfield", "United States", "742");
        date = LocalDate.of(1998, 6, 25);

        User admin = new Admin(null, "Admin", "admin", "1234", new ArrayList<>());
        userRepository.save(admin);

        roleService.addRoleToUser("admin", "ADMIN");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void saveAdmin_Valid_Created() throws Exception {
        Admin admin = new Admin(null, "Clara Mafe", "clara", "1234", new ArrayList<>());
        String body = objectMapper.writeValueAsString(admin);
        MvcResult mvcResult = mockMvc.perform(post("/api/admin/save")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void getAdmin_Valid_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/admin/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Nuria Mafe"));
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void updateAdmin_Valid_NoContent() throws Exception {
        Admin admin = adminRepository.findById(1L).get();
        admin.setName("Homer Simpson");
        String body = objectMapper.writeValueAsString(admin);
        MvcResult mvcResult = mockMvc.perform(put("/api/admin/{id}", 1L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals("Homer Simpson", admin.getName());
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void deleteAdmin_Valid_NoContent() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/api/admin/{id}", 1L))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}