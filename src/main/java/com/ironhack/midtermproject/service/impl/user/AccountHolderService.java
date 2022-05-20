package com.ironhack.midtermproject.service.impl.user;

import com.ironhack.midtermproject.model.user.AccountHolder;
import com.ironhack.midtermproject.repository.user.AccountHolderRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.service.interfaces.user.AccountHolderServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
public class AccountHolderService implements AccountHolderServiceInterface {
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private UserService userService;

    public AccountHolder saveAccountHolder(AccountHolder accountHolder) {
        log.info("Saving a new account holder account inside of the database");
        if (accountHolder.getId() != null) {
            Optional<AccountHolder> optionalAccountHolder = accountHolderRepository.findById(accountHolder.getId());
            if (optionalAccountHolder.isPresent())
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User with id " + accountHolder.getId() + " already exist");
        }
        return (AccountHolder) userService.saveUser(accountHolder);
    }

    public AccountHolder getAccountHolder(Long id) {
        log.info("Fetching account holder account {}", id);
        return accountHolderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account holder user not found"));
    }

    public void updateAccountHolder(Long id, AccountHolder accountHolder) {
        log.info("Updating account holder user {}", id);
        AccountHolder accountHolderFromDB = accountHolderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account holder user not found"));
        accountHolder.setId(accountHolderFromDB.getId());
        userService.updateUser(id, accountHolder);
    }

    public void deleteAccountHolder(Long id) {
        log.info("Deleting account holder user {}", id);
        accountHolderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account holder user not found"));
        if (accountHolderRepository.findById(id).isPresent()) {
            accountHolderRepository.deleteById(id);
        }
    }
}
