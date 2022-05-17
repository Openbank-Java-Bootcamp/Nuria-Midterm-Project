package com.ironhack.midtermproject.service.interfaces.account;

import com.ironhack.midtermproject.model.account.Checking;
import com.ironhack.midtermproject.utils.Money;
import org.springframework.stereotype.Service;

@Service
public interface CheckingServiceInterface {
    Checking saveChecking(Checking checking);
    Checking getChecking(Long id);
    void updateChecking(Long id, Checking checking);
    void deleteChecking(Long id);
    void updateBalance(Long id, Money balance);
}