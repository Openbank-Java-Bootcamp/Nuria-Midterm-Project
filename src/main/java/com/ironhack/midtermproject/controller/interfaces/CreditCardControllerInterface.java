package com.ironhack.midtermproject.controller.interfaces;


import com.ironhack.midtermproject.model.account.CreditCard;

public interface CreditCardControllerInterface {
    void saveCreditCard(CreditCard creditCard);
    CreditCard getCreditCard(Long id);
    void updateCreditCard(Long id, CreditCard creditCard);
    void deleteCreditCard(Long id);
}
