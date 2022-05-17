package com.ironhack.midtermproject.controller.impl.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.controller.interfaces.account.CreditCardControllerInterface;
import com.ironhack.midtermproject.model.account.CreditCard;
import com.ironhack.midtermproject.service.interfaces.account.CreditCardServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credit")
public class CreditCardController implements CreditCardControllerInterface {
    @Autowired
    private CreditCardServiceInterface creditCardService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCreditCard(@RequestBody(required=false) @Valid CreditCard creditCard) {
        creditCardService.saveCreditCard(creditCard);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreditCard getCreditCard(@PathVariable Long id) {
        return creditCardService.getCreditCard(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCreditCard(@PathVariable Long id, @RequestBody(required=false) @Valid CreditCard creditCard) {
        creditCardService.updateCreditCard(id, creditCard);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCreditCard(@PathVariable Long id) {
        creditCardService.deleteCreditCard(id);
    }

    @PatchMapping("/{id}/balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable Long id, @RequestBody @Valid BalanceDTO balanceDTO) {
        creditCardService.updateBalance(id, balanceDTO.getBalance());
    }
}
