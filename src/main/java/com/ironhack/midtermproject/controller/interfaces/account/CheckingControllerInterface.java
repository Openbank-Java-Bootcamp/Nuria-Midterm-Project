package com.ironhack.midtermproject.controller.interfaces.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.DTO.CheckingDTO;
import com.ironhack.midtermproject.model.account.Checking;

import java.math.BigDecimal;

public interface CheckingControllerInterface {
    void saveChecking(CheckingDTO checkingDTO);
    Checking getChecking(Long id);
    void updateChecking(Long id, Checking checking);
    void deleteChecking(Long id);
    void updateBalance(Long id, BalanceDTO balanceDTO);

    void transferMoney(String username, Long id, BigDecimal transfer);
}
