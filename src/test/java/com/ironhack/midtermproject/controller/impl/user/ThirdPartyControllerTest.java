package com.ironhack.midtermproject.controller.impl.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.model.user.ThirdParty;
import com.ironhack.midtermproject.model.user.Admin;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.repository.user.ThirdPartyRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.service.impl.user.RoleService;
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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class ThirdPartyControllerTest {
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
    private ThirdPartyRepository thirdPartyRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        ThirdParty thirdParty = new ThirdParty("third", "hashedkey");
        thirdPartyRepository.save(thirdParty);

        User admin = new Admin(null, "Admin", "admin", "1234", new ArrayList<>());
        userRepository.save(admin);

        roleService.addRoleToUser("admin", "ADMIN");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        thirdPartyRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void saveThirdParty_Valid_Created() throws Exception {
        ThirdParty third = new ThirdParty("clara", "1234");
        String body = objectMapper.writeValueAsString(third);
        MvcResult mvcResult = mockMvc.perform(post("/api/third/save")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void getThirdParty_Valid_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/third/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("third"));
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void updateThirdParty_Valid_NoContent() throws Exception {
        ThirdParty third = thirdPartyRepository.findById(1L).get();
        third.setName("Homer Simpson");
        String body = objectMapper.writeValueAsString(third);
        MvcResult mvcResult = mockMvc.perform(put("/api/third/{id}", 1L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals("Homer Simpson", third.getName());
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void deleteThirdParty_Valid_NoContent() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/api/third/{id}", 1L))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void transferMoney_Valid_NoContent() throws Exception {
        MvcResult mvcResult = mockMvc.perform(patch("/api/third/transfer/{hashedKey}/{id}/{amount}/{secretKey}",  "hashedkey", 1L, 40, 1234L))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void receiveMoney_Valid_NoContent() throws Exception {
        MvcResult mvcResult = mockMvc.perform(patch("/api/third/receive/{hashedKey}/{id}/{amount}/{secretKey}",  "hashedkey", 1L, 40, 1234L))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}