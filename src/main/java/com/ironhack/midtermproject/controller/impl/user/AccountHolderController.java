package com.ironhack.midtermproject.controller.impl.user;

import com.ironhack.midtermproject.controller.interfaces.user.AccountHolderControllerInterface;
import com.ironhack.midtermproject.model.user.AccountHolder;
import com.ironhack.midtermproject.service.interfaces.user.AccountHolderServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/holder")
public class AccountHolderController implements AccountHolderControllerInterface {
    @Autowired
    private AccountHolderServiceInterface accountHolderService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAccountHolder(@RequestBody(required=false) @Valid AccountHolder accountHolder) {
        accountHolderService.saveAccountHolder(accountHolder);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountHolder getAccountHolder(@PathVariable Long id) {
        return accountHolderService.getAccountHolder(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccountHolder(@PathVariable Long id, @RequestBody(required=false) @Valid AccountHolder accountHolder) {
        accountHolderService.updateAccountHolder(id, accountHolder);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountHolder(@PathVariable Long id) {
        accountHolderService.deleteAccountHolder(id);
    }
}
