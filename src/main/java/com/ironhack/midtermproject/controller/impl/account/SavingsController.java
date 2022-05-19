package com.ironhack.midtermproject.controller.impl.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.DTO.SavingsDTO;
import com.ironhack.midtermproject.controller.interfaces.account.SavingsControllerInterface;
import com.ironhack.midtermproject.model.account.Savings;
import com.ironhack.midtermproject.service.interfaces.account.SavingsServiceInterface;
import com.ironhack.midtermproject.utils.Money;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/savings")
public class SavingsController implements SavingsControllerInterface {
    @Autowired
    private SavingsServiceInterface savingsService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSavings(@RequestBody(required=false) @Valid SavingsDTO savingsDTO) {
        savingsService.saveSavings(savingsDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Savings getSavings(@PathVariable Long id) {
        return savingsService.getSavings(id);
    }

    @GetMapping("/{id}/balance/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Money getSavingsBalance(@PathVariable Long id, @PathVariable String username) {
        return savingsService.getSavingsBalance(id, username);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSavings(@PathVariable Long id, @RequestBody(required=false) @Valid SavingsDTO savingsDTO) {
        savingsService.updateSavings(id, savingsDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSavings(@PathVariable Long id) {
        savingsService.deleteSavings(id);
    }

    @PatchMapping("/{username}/{id}/balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable String username, @PathVariable Long id, @RequestBody @Valid BalanceDTO balanceDTO) {
        savingsService.updateBalance(username, id, balanceDTO.getBalance());
    }

    @PatchMapping("/transfer/{username}/{id}/{transfer}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(@PathVariable String username, @PathVariable Long id, @PathVariable BigDecimal transfer) {
        savingsService.transferMoney(username, id, transfer);
    }
}
