package com.ironhack.midtermproject.controller.impl.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.model.account.Checking;
import com.ironhack.midtermproject.model.user.AccountHolder;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.repository.account.CheckingRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.service.impl.user.RoleService;
import com.ironhack.midtermproject.utils.Address;
import com.ironhack.midtermproject.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CheckingControllerTest {
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
    private CheckingRepository checkingRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Address address = new Address("Evergreen Terrace", "Springfield", "United States", "742");
        LocalDate date = LocalDate.of(1998, 6, 25);

        User user = new AccountHolder("Nuria Mafe", "nuria", "1234", date, address);
        userRepository.save(user);

        roleService.addRoleToUser("nuria", "HOLDER");

        Checking checking = new Checking(1234L, new Money(new BigDecimal(1400)), user, new Money(new BigDecimal(300)), new Money(new BigDecimal(10)));
        checkingRepository.save(checking);
    }

    @AfterEach
    void tearDown() {
        checkingRepository.deleteAll();
        userRepository.deleteAll();
    }

    // Test the penalty fee
    @Test
    @WithMockUser(username = "nuria", password = "1234", roles = "HOLDER")
    void updateBalance_validData_NoContent() throws Exception {
        Checking checking = checkingRepository.findById(1L).get();
        checking.setBalance(new Money(new BigDecimal(200)));
        String body = objectMapper.writeValueAsString(checking);
        MvcResult mvcResult = mockMvc.perform(patch("/api/checking/{username}/{id}/balance", "nuria", 1L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals("160.00", checking.getBalance().getAmount().toString());
    }
}