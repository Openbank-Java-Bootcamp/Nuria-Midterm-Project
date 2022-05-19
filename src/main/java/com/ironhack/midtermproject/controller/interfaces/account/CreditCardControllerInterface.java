package com.ironhack.midtermproject.controller.interfaces.account;


import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.DTO.CreditCardDTO;
import com.ironhack.midtermproject.model.account.CreditCard;
import com.ironhack.midtermproject.utils.Money;

import java.math.BigDecimal;

public interface CreditCardControllerInterface {
    void saveCreditCard(CreditCardDTO creditCardDTO);
    CreditCard getCreditCard(Long id);
    Money getCreditBalance(Long id, String username);
    void updateCreditCard(Long id, CreditCardDTO creditCardDTO);
    void deleteCreditCard(Long id);
    void updateBalance(Long id, BalanceDTO balanceDTO);
    void transferMoney(String username, Long id, BigDecimal transfer);
}
