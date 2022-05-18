package com.ironhack.midtermproject.service.interfaces.account;

import com.ironhack.midtermproject.DTO.SavingsDTO;
import com.ironhack.midtermproject.model.account.Savings;
import com.ironhack.midtermproject.utils.Money;

import java.math.BigDecimal;

public interface SavingsServiceInterface {
    Savings saveSavings(SavingsDTO savingsDTO);
    Savings getSavings(Long id);
    void updateSavings(Long id, Savings savings);
    void deleteSavings(Long id);
    void updateBalance(Long id, Money balance);
    void transferMoney(String name, Long id, BigDecimal transfer);
}
