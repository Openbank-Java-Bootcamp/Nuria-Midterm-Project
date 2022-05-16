package com.ironhack.midtermproject.service.impl;

import com.ironhack.midtermproject.model.account.CreditCard;
import com.ironhack.midtermproject.repository.CreditCardRepository;
import com.ironhack.midtermproject.service.interfaces.CreditCardServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
public class CreditCardService implements CreditCardServiceInterface {
    @Autowired
    private CreditCardRepository creditCardRepository;

    public CreditCard saveCreditCard(CreditCard creditCard) {
        log.info("Saving a new credit card account {} inside of the database", creditCard.getAccountId());
        if (creditCard.getAccountId() != null) {
            Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(creditCard.getAccountId());
            if (optionalCreditCard.isPresent())
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account with id " + creditCard.getAccountId() + " already exist");
        }
        return creditCardRepository.save(creditCard);
    }

    public CreditCard getCreditCard(Long id) {
        log.info("Fetching a credit card checking account");
        return creditCardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card account not found"));
    }

    public void updateCreditCard(Long id, CreditCard creditCard) {
        CreditCard creditCardFromDB = creditCardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card account not found"));
        creditCard.setAccountId(creditCardFromDB.getAccountId());
        creditCardRepository.save(creditCard);
    }

    public void deleteCreditCard(Long id) {
        creditCardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card account not found"));
        if (creditCardRepository.findById(id).isPresent()) {
            creditCardRepository.deleteById(id);
        }
    }
}
