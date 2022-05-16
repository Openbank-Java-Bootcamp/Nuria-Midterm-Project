package com.ironhack.midtermproject.controller.interfaces;

import com.ironhack.midtermproject.model.account.Checking;

public interface CheckingControllerInterface {
    void saveChecking(Checking checking);
    Checking getChecking(Long id);
    void updateChecking(Long id, Checking checking);
    void deleteChecking(Long id);
}
