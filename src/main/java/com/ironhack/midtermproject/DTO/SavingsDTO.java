package com.ironhack.midtermproject.DTO;

import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingsDTO {
    private Long secretKey;
    private Money balance;
    private Long primaryOwner;
    private Long secondaryOwner;
    private Money minimumBalanceSavings;
    private BigDecimal interestRateSavings;

    public SavingsDTO(Long secretKey, Money balance, Long primaryOwner, Money minimumBalanceSavings, BigDecimal interestRateSavings) {
        this.secretKey = secretKey;
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.minimumBalanceSavings = minimumBalanceSavings;
        this.interestRateSavings = interestRateSavings;
    }
}
