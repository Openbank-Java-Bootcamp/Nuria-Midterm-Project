package com.ironhack.midtermproject.controller.interfaces.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.model.account.Savings;

public interface SavingsControllerInterface {
    void saveSavings(Savings savings);
    Savings getSavings(Long id);
    void updateSavings(Long id, Savings savings);
    void deleteSavings(Long id);
    void updateBalance(Long id, BalanceDTO balanceDTO);
}
