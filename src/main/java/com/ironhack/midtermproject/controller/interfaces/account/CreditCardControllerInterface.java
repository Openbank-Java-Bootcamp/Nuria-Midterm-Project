package com.ironhack.midtermproject.controller.interfaces.account;


import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.DTO.CreditCardDTO;
import com.ironhack.midtermproject.model.account.CreditCard;

public interface CreditCardControllerInterface {
    void saveCreditCard(CreditCardDTO creditCardDTO);
    CreditCard getCreditCard(Long id);
    void updateCreditCard(Long id, CreditCard creditCard);
    void deleteCreditCard(Long id);
    void updateBalance(Long id, BalanceDTO balanceDTO);
}
