package com.ironhack.midtermproject.controller.interfaces.user;

import com.ironhack.midtermproject.model.user.AccountHolder;

public interface AccountHolderControllerInterface {
    void saveAccountHolder(AccountHolder accountHolder);
    AccountHolder getAccountHolder(Long id);
    void updateAccountHolder(Long id, AccountHolder accountHolder);
    void deleteAccountHolder(Long id);
}
