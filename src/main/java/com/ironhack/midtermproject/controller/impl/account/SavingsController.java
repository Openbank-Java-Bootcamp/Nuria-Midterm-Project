package com.ironhack.midtermproject.controller.impl.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.DTO.SavingsDTO;
import com.ironhack.midtermproject.controller.interfaces.account.SavingsControllerInterface;
import com.ironhack.midtermproject.model.account.Savings;
import com.ironhack.midtermproject.service.interfaces.account.SavingsServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSavings(@PathVariable Long id, @RequestBody(required=false) @Valid Savings savings) {
        savingsService.updateSavings(id, savings);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSavings(@PathVariable Long id) {
        savingsService.deleteSavings(id);
    }

    @PatchMapping("/{id}/balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable Long id, @RequestBody @Valid BalanceDTO balanceDTO) {
        savingsService.updateBalance(id, balanceDTO.getBalance());
    }
}
