package com.ironhack.midtermproject.controller.interfaces.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.model.account.Checking;

public interface CheckingControllerInterface {
    void saveChecking(Checking checking);
    Checking getChecking(Long id);
    void updateChecking(Long id, Checking checking);
    void deleteChecking(Long id);
    void updateBalance(Long id, BalanceDTO balanceDTO);
}
