package com.ironhack.midtermproject.controller.impl.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.DTO.CheckingDTO;
import com.ironhack.midtermproject.model.account.Checking;
import com.ironhack.midtermproject.model.user.AccountHolder;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.repository.account.CheckingRepository;
import com.ironhack.midtermproject.repository.account.StudentCheckingRepository;
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
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
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
    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Address address = new Address("Evergreen Terrace", "Springfield", "United States", "742");
        LocalDate date = LocalDate.of(1990, 6, 25);

        User user = new AccountHolder("Nuria Mafe", "nuria", "1234", date, address);
        userRepository.save(user);

        User user2 = new AccountHolder("Clara Mafe", "clara", "1234", date, address);
        userRepository.save(user2);

        User admin = new AccountHolder("Admin", "admin", "1234", date, address);
        userRepository.save(admin);

        roleService.addRoleToUser("nuria", "HOLDER");
        roleService.addRoleToUser("admin", "ADMIN");

        Checking checking = new Checking(1234L, new Money(new BigDecimal(1400)), user, new Money(new BigDecimal(300)), new Money(new BigDecimal(10)));
        checkingRepository.save(checking);
        Checking checkingReceiver = new Checking(1234L, new Money(new BigDecimal(1400)), user2, new Money(new BigDecimal(300)), new Money(new BigDecimal(10)));
        checkingRepository.save(checkingReceiver);
    }

    @AfterEach
    void tearDown() {
        checkingRepository.deleteAll();
        studentCheckingRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     *  Role admin
     */
    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void saveChecking_Valid_Created() throws Exception {
        CheckingDTO checking = new CheckingDTO(1234L, new Money(new BigDecimal(1400)), 1L, new Money(new BigDecimal(300)), new Money(new BigDecimal(10)));
        String body = objectMapper.writeValueAsString(checking);
        MvcResult mvcResult = mockMvc.perform(post("/api/checking/save")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void getChecking_Valid_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/checking/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Nuria Mafe"));
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void updateChecking_Valid_NoContent() throws Exception {
        Checking checking = checkingRepository.findById(1L).get();
        checking.updateBalance(new BigDecimal("1600"));
        CheckingDTO checkingDTO = new CheckingDTO(checking.getSecretKey(), checking.getBalance(), checking.getPrimaryOwner().getId(), checking.getMinimumBalanceChecking(), checking.getMonthlyMaintenanceFeeChecking());
        String body = objectMapper.writeValueAsString(checkingDTO);
        MvcResult mvcResult = mockMvc.perform(put("/api/checking/{id}", 1L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals("1600", checkingDTO.getBalance().getAmount().toString());
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void updateBalanceAdmin_Valid_NoContent() throws Exception {
        Checking checking = checkingRepository.findById(1L).get();
        checking.setBalance(new Money(new BigDecimal(1600)));
        String body = objectMapper.writeValueAsString(checking);
        MvcResult mvcResult = mockMvc.perform(patch("/api/checking/{username}/{id}/balance", "admin", 1L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals("1600.00", checking.getBalance().getAmount().toString());
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void deleteChecking_Valid_NoContent() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/api/checking/{id}", 1L))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    /**
     *  Role holder
     */
    @Test
    @WithMockUser(username = "nuria", password = "1234", roles = "HOLDER")
    void getBalance_Valid_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/checking/{id}/balance/{username}", 1L, "nuria"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("USD"));
    }

    // Test the penalty fee
    @Test
    @WithMockUser(username = "nuria", password = "1234", roles = "HOLDER")
    void updateBalance_Valid_NoContent() throws Exception {
        Checking checking = checkingRepository.findById(1L).get();
        checking.setBalance(new Money(new BigDecimal(200)));
        String body = objectMapper.writeValueAsString(checking);
        MvcResult mvcResult = mockMvc.perform(patch("/api/checking/{username}/{id}/balance", "nuria", 1L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals("160.00", checking.getBalance().getAmount().toString());
    }

    @Test
    @WithMockUser(username = "nuria", password = "1234", roles = "HOLDER")
    void transferMoney_Valid_NoContent() throws Exception {
        MvcResult mvcResult = mockMvc.perform(patch("/api/checking/transfer/{username}/{id}/{transfer}",  "nuria", 2L, 40))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}