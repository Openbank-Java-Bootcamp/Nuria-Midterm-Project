package com.ironhack.midtermproject.service.interfaces.account;

import com.ironhack.midtermproject.model.account.Savings;
import com.ironhack.midtermproject.utils.Money;

public interface SavingsServiceInterface {
    Savings saveSavings(Savings savings);
    Savings getSavings(Long id);
    void updateSavings(Long id, Savings savings);
    void deleteSavings(Long id);
    void updateBalance(Long id, Money balance);
}
