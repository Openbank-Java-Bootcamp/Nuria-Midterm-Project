package com.ironhack.midtermproject.service.interfaces;

import com.ironhack.midtermproject.model.account.Savings;

public interface SavingsServiceInterface {
    Savings saveSavings(Savings savings);
    Savings getSavings(Long id);
    void updateSavings(Long id, Savings savings);
    void deleteSavings(Long id);
}
