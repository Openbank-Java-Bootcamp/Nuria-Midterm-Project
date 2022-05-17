package com.ironhack.midtermproject.service.interfaces.user;


import com.ironhack.midtermproject.model.user.AccountHolder;

public interface AccountHolderServiceInterface {
    AccountHolder saveAccountHolder(AccountHolder accountHolder);
    AccountHolder getAccountHolder(Long id);
    void updateAccountHolder(Long id, AccountHolder accountHolder);
    void deleteAccountHolder(Long id);
}
