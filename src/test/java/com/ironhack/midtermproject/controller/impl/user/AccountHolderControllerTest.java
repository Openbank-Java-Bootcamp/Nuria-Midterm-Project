package com.ironhack.midtermproject.controller.impl.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.model.user.AccountHolder;
import com.ironhack.midtermproject.model.user.Admin;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.repository.user.AccountHolderRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class AccountHolderControllerTest {
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
    private AccountHolderRepository accountHolderRepository;

    Address address;
    LocalDate date;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        address = new Address("Evergreen Terrace", "Springfield", "United States", "742");
        date = LocalDate.of(1998, 6, 25);

        User user = new AccountHolder("Nuria Mafe", "nuria", "1234", date, address);
        userRepository.save(user);

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
    void saveAccountHolder_Valid_Created() throws Exception {
        AccountHolder holder = new AccountHolder("Clara Mafe", "clara", "1234", date, address);
        String body = objectMapper.writeValueAsString(holder);
        MvcResult mvcResult = mockMvc.perform(post("/api/holder/save")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void getAccountHolder_Valid_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/holder/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Nuria Mafe"));
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void updateAccountHolder_Valid_NoContent() throws Exception {
        AccountHolder holder = accountHolderRepository.findById(1L).get();
        holder.setName("Homer Simpson");
        String body = objectMapper.writeValueAsString(holder);
        MvcResult mvcResult = mockMvc.perform(put("/api/holder/{id}", 1L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals("Homer Simpson", holder.getName());
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void deleteAccountHolder_Valid_NoContent() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/api/holder/{id}", 1L))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}