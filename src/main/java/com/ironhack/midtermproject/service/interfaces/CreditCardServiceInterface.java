package com.ironhack.midtermproject.service.interfaces;

import com.ironhack.midtermproject.model.account.CreditCard;

public interface CreditCardServiceInterface {
    CreditCard saveCreditCard(CreditCard creditCard);
    CreditCard getCreditCard(Long id);
    void updateCreditCard(Long id, CreditCard creditCard);
    void deleteCreditCard(Long id);
}
