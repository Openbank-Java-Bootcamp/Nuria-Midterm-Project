package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.controller.interfaces.SavingsControllerInterface;
import com.ironhack.midtermproject.model.account.Savings;
import com.ironhack.midtermproject.service.interfaces.SavingsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/savings")
public class SavingsController implements SavingsControllerInterface {
    @Autowired
    private SavingsServiceInterface savingsService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSavings(@RequestBody Savings savings) {
        savingsService.saveSavings(savings);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Savings getSavings(@PathVariable Long id) {
        return savingsService.getSavings(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSavings(@PathVariable Long id, @RequestBody Savings savings) {
        savingsService.updateSavings(id, savings);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSavings(@PathVariable Long id) {
        savingsService.deleteSavings(id);
    }
}
