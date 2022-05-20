package com.ironhack.midtermproject.controller.impl.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.model.account.Savings;
import com.ironhack.midtermproject.model.user.AccountHolder;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.repository.account.SavingsRepository;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SavingsControllerTest {
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
    private SavingsRepository savingsRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Address address = new Address("Evergreen Terrace", "Springfield", "United States", "742");
        LocalDate date = LocalDate.of(1998, 6, 25);

        User user = new AccountHolder("Nuria Mafe", "nuria", "1234", date, address);
        userRepository.save(user);

        roleService.addRoleToUser("nuria", "HOLDER");

        Savings savings = new Savings(1234L, new Money(new BigDecimal(1400)), user, new Money(new BigDecimal(300)), new BigDecimal(0.4).setScale(1, RoundingMode.HALF_EVEN));
        Savings savingsCreationDate = new Savings(1234L, LocalDate.of(2020, 03, 12), new Money(new BigDecimal(1400)), user, new Money(new BigDecimal(300)), new BigDecimal(0.4).setScale(1, RoundingMode.HALF_EVEN));
        Savings savingsInterestRate = new Savings(1234L, new Money(new BigDecimal(1400)), user, new Money(new BigDecimal(300)), new BigDecimal(0.4).setScale(1, RoundingMode.HALF_EVEN), LocalDate.of(2012, 05, 12));
        savingsRepository.saveAll(List.of(savings,savingsCreationDate, savingsInterestRate));
    }

    @AfterEach
    void tearDown() {
        savingsRepository.deleteAll();
        userRepository.deleteAll();
    }

    // Test the penalty fee
    @Test
    @WithMockUser(username = "nuria", password = "1234", roles = "HOLDER")
    void updateBalancePenalty_validData_NoContent() throws Exception {
        Savings savings = savingsRepository.findById(1L).get();
        savings.setBalance(new Money(new BigDecimal(200)));
        String body = objectMapper.writeValueAsString(savings);
        MvcResult mvcResult = mockMvc.perform(patch("/api/savings/{username}/{id}/balance", "nuria", 1L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals("160.00", savings.getBalance().getAmount().toString());
    }

    // Test add interest when it has been a year since the account was created
    @Test
    @WithMockUser(username = "nuria", password = "1234", roles = "HOLDER")
    void updateBalanceCreation_validData_NoContent() throws Exception {
        Savings savings = savingsRepository.findById(2L).get();
        savings.setBalance(new Money(new BigDecimal(1400)));
        String body = objectMapper.writeValueAsString(savings);
        MvcResult mvcResult = mockMvc.perform(patch("/api/savings/{username}/{id}/balance", "nuria", 2L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals("1960.000", savings.getBalance().getAmount().toString());
    }

    // Test add interest when it has been a year since the account was created
    @Test
    @WithMockUser(username = "nuria", password = "1234", roles = "HOLDER")
    void updateBalanceInterest_validData_NoContent() throws Exception {
        Savings savings = savingsRepository.findById(3L).get();
        savings.setBalance(new Money(new BigDecimal(1400)));
        String body = objectMapper.writeValueAsString(savings);
        MvcResult mvcResult = mockMvc.perform(patch("/api/savings/{username}/{id}/balance", "nuria", 3L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals("1960.000", savings.getBalance().getAmount().toString());
    }
}