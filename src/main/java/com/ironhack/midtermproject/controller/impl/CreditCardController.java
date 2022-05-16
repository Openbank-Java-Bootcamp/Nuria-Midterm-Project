package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.controller.interfaces.CreditCardControllerInterface;
import com.ironhack.midtermproject.model.account.CreditCard;
import com.ironhack.midtermproject.service.interfaces.CreditCardServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credit_card")
public class CreditCardController implements CreditCardControllerInterface {
    @Autowired
    private CreditCardServiceInterface creditCardService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCreditCard(@RequestBody CreditCard creditCard) {
        creditCardService.saveCreditCard(creditCard);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreditCard getCreditCard(@PathVariable Long id) {
        return creditCardService.getCreditCard(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCreditCard(@PathVariable Long id, @RequestBody CreditCard creditCard) {
        creditCardService.updateCreditCard(id, creditCard);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCreditCard(@PathVariable Long id) {
        creditCardService.deleteCreditCard(id);
    }
}
