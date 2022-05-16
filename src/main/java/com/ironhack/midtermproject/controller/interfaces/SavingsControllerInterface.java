package com.ironhack.midtermproject.controller.interfaces;

import com.ironhack.midtermproject.model.account.Savings;

public interface SavingsControllerInterface {
    void saveSavings(Savings savings);
    Savings getSavings(Long id);
    void updateSavings(Long id, Savings savings);
    void deleteSavings(Long id);
}
