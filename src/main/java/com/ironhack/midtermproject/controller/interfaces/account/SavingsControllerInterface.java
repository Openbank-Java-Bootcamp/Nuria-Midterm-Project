package com.ironhack.midtermproject.controller.interfaces.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.DTO.SavingsDTO;
import com.ironhack.midtermproject.model.account.Savings;
import com.ironhack.midtermproject.utils.Money;

import java.math.BigDecimal;

public interface SavingsControllerInterface {
    void saveSavings(SavingsDTO savingsDTO);
    Savings getSavings(Long id);
    Money getSavingsBalance(Long id, String username);
    void updateSavings(Long id, SavingsDTO savingsDTO);
    void deleteSavings(Long id);
    void updateBalance(Long id, BalanceDTO balanceDTO);
    void transferMoney(String username, Long id, BigDecimal transfer);
}
