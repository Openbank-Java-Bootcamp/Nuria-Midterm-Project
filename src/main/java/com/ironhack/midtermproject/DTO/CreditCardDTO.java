package com.ironhack.midtermproject.DTO;

import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardDTO {
    private Long secretKey;
    private Money balance;
    private Long primaryOwner;
    private Long secondaryOwner;
    private Money creditLimit;
    private BigDecimal interestRateCredit;

    public CreditCardDTO(Long secretKey, Money balance, Long primaryOwner, Money creditLimit, BigDecimal interestRateCredit) {
        this.secretKey = secretKey;
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.creditLimit = creditLimit;
        this.interestRateCredit = interestRateCredit;
    }
}
