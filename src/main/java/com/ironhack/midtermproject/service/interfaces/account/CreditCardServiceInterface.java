package com.ironhack.midtermproject.service.interfaces.account;

import com.ironhack.midtermproject.DTO.CreditCardDTO;
import com.ironhack.midtermproject.model.account.CreditCard;
import com.ironhack.midtermproject.utils.Money;

import java.math.BigDecimal;

public interface CreditCardServiceInterface {
    CreditCard saveCreditCard(CreditCardDTO creditCardDTO);
    CreditCard getCreditCard(Long id);
    Money getCreditCardBalance(Long id, String username);
    void updateCreditCard(Long id, CreditCardDTO creditCardDTO);
    void deleteCreditCard(Long id);
    void updateBalance(String username, Long id, Money balance);
    void transferMoney(String name, Long id, BigDecimal transfer);
}
