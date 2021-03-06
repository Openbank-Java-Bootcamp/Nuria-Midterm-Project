package com.ironhack.midtermproject.controller.impl.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.DTO.CreditCardDTO;
import com.ironhack.midtermproject.model.account.CreditCard;
import com.ironhack.midtermproject.model.user.AccountHolder;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.repository.account.CreditCardRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class CreditCardControllerTest {
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
    private CreditCardRepository creditCardRepository;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Address address = new Address("Evergreen Terrace", "Springfield", "United States", "742");
        LocalDate date = LocalDate.of(1998, 6, 25);

        User user = new AccountHolder("Nuria Mafe", "nuria", "1234", date, address);
        userRepository.save(user);

        User user2 = new AccountHolder("Clara Mafe", "clara", "1234", date, address);
        userRepository.save(user2);

        User admin = new AccountHolder("Admin", "admin", "1234", date, address);
        userRepository.save(admin);

        roleService.addRoleToUser("nuria", "HOLDER");
        roleService.addRoleToUser("admin", "ADMIN");

        CreditCard creditCard = new CreditCard(1234L, new Money(new BigDecimal(1400)), user, new Money(new BigDecimal(300)), new BigDecimal(0.4).setScale(1, RoundingMode.HALF_EVEN));
        CreditCard creditCard2 = new CreditCard(1234L, new Money(new BigDecimal(1400)), user2, new Money(new BigDecimal(300)), new BigDecimal(0.4).setScale(1, RoundingMode.HALF_EVEN));
        CreditCard creditCardCreationDate = new CreditCard(1234L, LocalDate.of(2020, 03, 12), new Money(new BigDecimal(1400)), user2, new Money(new BigDecimal(300)), new BigDecimal(0.4).setScale(1, RoundingMode.HALF_EVEN));
        CreditCard creditCardInterestRate = new CreditCard(1234L, new Money(new BigDecimal(1400)), user2, new Money(new BigDecimal(300)), new BigDecimal(0.4).setScale(1, RoundingMode.HALF_EVEN), LocalDate.of(2012, 05, 12));
        creditCardRepository.saveAll(List.of(creditCard, creditCard2, creditCardCreationDate, creditCardInterestRate));
    }

    @AfterEach
    void tearDown() {
        creditCardRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     *  Role admin
     */
    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void saveCreditCard_Valid_Created() throws Exception {
        CreditCardDTO creditCard = new CreditCardDTO(1234L, new Money(new BigDecimal(1400)), 1L, new Money(new BigDecimal(300)), new BigDecimal(0.4).setScale(1, RoundingMode.HALF_EVEN));
        String body = objectMapper.writeValueAsString(creditCard);
        MvcResult mvcResult = mockMvc.perform(post("/api/credit/save")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void getCreditCard_Valid_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/credit/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Nuria Mafe"));
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void updateCreditCard_Valid_NoContent() throws Exception {
        CreditCard creditCard = creditCardRepository.findById(1L).get();
        creditCard.updateBalance(new BigDecimal("1600"));
        CreditCardDTO creditCardDTO = new CreditCardDTO(creditCard.getSecretKey(), creditCard.getBalance(), creditCard.getPrimaryOwner().getId(), creditCard.getCreditLimit(), creditCard.getInterestRateCredit());
        String body = objectMapper.writeValueAsString(creditCardDTO);
        MvcResult mvcResult = mockMvc.perform(put("/api/credit/{id}", 1L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals("1600", creditCardDTO.getBalance().getAmount().toString());
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void updateBalanceAdmin_Valid_NoContent() throws Exception {
        CreditCard creditCard = creditCardRepository.findById(1L).get();
        creditCard.setBalance(new Money(new BigDecimal(1600)));
        String body = objectMapper.writeValueAsString(creditCard);
        MvcResult mvcResult = mockMvc.perform(patch("/api/credit/{username}/{id}/balance", "admin", 1L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals("1600.00", creditCard.getBalance().getAmount().toString());
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void deleteCreditCard_Valid_NoContent() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/api/credit/{id}", 1L))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    /**
     *  Role holder
     */
    @Test
    @WithMockUser(username = "nuria", password = "1234", roles = "HOLDER")
    void getBalance_Valid_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/credit/{id}/balance/{username}", 1L, "nuria"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("USD"));
    }

    // Test add interest when it has been a year since the account was created
    @Test
    @WithMockUser(username = "nuria", password = "1234", roles = "HOLDER")
    void updateBalanceCreation_validData_NoContent() throws Exception {
        CreditCard creditCard = creditCardRepository.findById(3L).get();
        creditCard.setBalance(new Money(new BigDecimal(1400)));
        String body = objectMapper.writeValueAsString(creditCard);
        MvcResult mvcResult = mockMvc.perform(patch("/api/credit/{username}/{id}/balance", "nuria", 2L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals("1960.000", creditCard.getBalance().getAmount().toString());
    }

    // Test add interest when it has been a year since the account was created
    @Test
    @WithMockUser(username = "nuria", password = "1234", roles = "HOLDER")
    void updateBalanceInterest_validData_NoContent() throws Exception {
        CreditCard creditCard = creditCardRepository.findById(4L).get();
        creditCard.setBalance(new Money(new BigDecimal(1400)));
        String body = objectMapper.writeValueAsString(creditCard);
        MvcResult mvcResult = mockMvc.perform(patch("/api/credit/{username}/{id}/balance", "nuria", 3L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals("1960.000", creditCard.getBalance().getAmount().toString());
    }

    @Test
    @WithMockUser(username = "nuria", password = "1234", roles = "HOLDER")
    void transferMoney_Valid_NoContent() throws Exception {
        MvcResult mvcResult = mockMvc.perform(patch("/api/credit/transfer/{username}/{id}/{transfer}",  "nuria", 2L, 40))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}