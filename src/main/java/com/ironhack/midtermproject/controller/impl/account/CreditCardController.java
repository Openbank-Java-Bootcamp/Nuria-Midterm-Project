package com.ironhack.midtermproject.controller.impl.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.DTO.CreditCardDTO;
import com.ironhack.midtermproject.controller.interfaces.account.CreditCardControllerInterface;
import com.ironhack.midtermproject.model.account.CreditCard;
import com.ironhack.midtermproject.service.interfaces.account.CreditCardServiceInterface;
import com.ironhack.midtermproject.utils.Money;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/credit")
public class CreditCardController implements CreditCardControllerInterface {
    @Autowired
    private CreditCardServiceInterface creditCardService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCreditCard(@RequestBody(required=false) @Valid CreditCardDTO creditCardDTO) {
        creditCardService.saveCreditCard(creditCardDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreditCard getCreditCard(@PathVariable Long id) {
        return creditCardService.getCreditCard(id);
    }

    @GetMapping("/{id}/balance/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Money getCreditBalance(@PathVariable Long id, @PathVariable String username) {
        return creditCardService.getCreditCardBalance(id, username);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCreditCard(@PathVariable Long id, @RequestBody(required=false) @Valid CreditCardDTO creditCardDTO) {
        creditCardService.updateCreditCard(id, creditCardDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCreditCard(@PathVariable Long id) {
        creditCardService.deleteCreditCard(id);
    }

    @PatchMapping("/{username}/{id}/balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable String username, @PathVariable Long id, @RequestBody @Valid BalanceDTO balanceDTO) {
        creditCardService.updateBalance(username, id, balanceDTO.getBalance());
    }

    @PatchMapping("/transfer/{username}/{id}/{transfer}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(@PathVariable String username, @PathVariable Long id, @PathVariable BigDecimal transfer) {
        creditCardService.transferMoney(username, id, transfer);
    }
}
