package com.ironhack.midtermproject.controller.interfaces.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.DTO.CheckingDTO;
import com.ironhack.midtermproject.model.account.Checking;
import com.ironhack.midtermproject.utils.Money;

import java.math.BigDecimal;

public interface CheckingControllerInterface {
    void saveChecking(CheckingDTO checkingDTO);
    Checking getChecking(Long id);
    Money getCheckingBalance(Long id, String username);
    void updateChecking(Long id, CheckingDTO checkingDTO);
    void deleteChecking(Long id);
    void updateBalance(Long id, BalanceDTO balanceDTO);

    void transferMoney(String username, Long id, BigDecimal transfer);
}
