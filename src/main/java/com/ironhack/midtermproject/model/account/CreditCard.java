package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.utils.Money;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "credit_card")
@PrimaryKeyJoinColumn(name = "id_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard extends Account {
    @Column(name = "credit_limit")
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount"))
    })
    @Embedded
    private Money creditLimit;
    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    private static final BigDecimal LIMIT_CREDIT = new BigDecimal(100000);
    private static final BigDecimal LIMIT_INTEREST_RATE = new BigDecimal(0.1);

    public CreditCard(Money balance, User primaryOwner, User secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
        this.creditLimit = new Money(new BigDecimal(100));
        this.interestRate = new BigDecimal(0.2);
    }

    public CreditCard(Money balance, User primaryOwner) {
        super(balance, primaryOwner);
        this.creditLimit = new Money(new BigDecimal(100));
        this.interestRate = new BigDecimal(0.2);
    }

    public CreditCard(Money balance, User primaryOwner, User secondaryOwner, Money creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }

    public CreditCard(Money balance, User primaryOwner, Money creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }

    public void setCreditLimit(Money creditLimit) {
        do {
            try {
        this.creditLimit = creditLimit;
            } catch (Exception e) {
                System.err.println("The maximum limit credit is " + LIMIT_CREDIT);
            }
        } while (creditLimit.getAmount().compareTo(LIMIT_CREDIT) == 1); // While the limit credit is greater than THE limit
    }

    public void setInterestRate(BigDecimal interestRate) {
        do {
            try {
        this.interestRate = interestRate;
            } catch (Exception e) {
                System.err.println("The minimum interest rate is " + LIMIT_INTEREST_RATE);
            }
        } while (interestRate.compareTo(LIMIT_INTEREST_RATE) == -1); // While the interest is greater than 0.5
    }
}
