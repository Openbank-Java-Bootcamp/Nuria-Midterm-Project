package com.ironhack.midtermproject.service.interfaces.account;

import com.ironhack.midtermproject.DTO.CheckingDTO;
import com.ironhack.midtermproject.model.account.Checking;
import com.ironhack.midtermproject.utils.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface CheckingServiceInterface {
    Checking saveChecking(CheckingDTO checking);
    Checking getChecking(Long id);
    Money getCheckingBalance(Long id, String username);
    void updateChecking(Long id, CheckingDTO checkingDTO);
    void deleteChecking(Long id);
    void updateBalance(String username, Long id, Money balance);
    void transferMoney(String name, Long id, BigDecimal transfer);
}
