package com.ironhack.midtermproject.service.interfaces.account;

import com.ironhack.midtermproject.model.account.CreditCard;
import com.ironhack.midtermproject.utils.Money;

public interface CreditCardServiceInterface {
    CreditCard saveCreditCard(CreditCard creditCard);
    CreditCard getCreditCard(Long id);
    void updateCreditCard(Long id, CreditCard creditCard);
    void deleteCreditCard(Long id);
    void updateBalance(Long id, Money balance);
}
