package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.utils.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "credit_card")
@PrimaryKeyJoinColumn(name = "id_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class CreditCard extends Account {
    @Column(name = "credit_limit")
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount"))
    })
    @Embedded
    @NotEmpty(message = "You must have a credit limit")
    private Money creditLimit;
    @Column(name = "interest_rate", precision = 32, scale = 4)
    //@Digits()
    @NotEmpty(message = "You must have a interest rate")
    private BigDecimal interestRateCredit;
    @Column(name = "creation_date")
    private LocalDate creationDateCredit;

    private LocalDate interestAddedDate;

    private boolean firstTimeAdded = false;

    private static final BigDecimal LIMIT_CREDIT = new BigDecimal(100000);
    private static final BigDecimal LIMIT_INTEREST_RATE = new BigDecimal(0.1);

    // Constructor with primary, secondary owners, and default values
    public CreditCard(Money balance, User primaryOwner, User secondaryOwner, LocalDate creationDateCredit) {
        super(balance, primaryOwner, secondaryOwner);
        this.creditLimit = new Money(new BigDecimal(100));
        this.interestRateCredit = new BigDecimal(0.2);
        this.creationDateCredit = creationDateCredit;
    }

    // Constructor with primary and default values
    public CreditCard(Money balance, User primaryOwner, LocalDate creationDateCredit) {
        super(balance, primaryOwner);
        this.creditLimit = new Money(new BigDecimal(100));
        this.interestRateCredit = new BigDecimal(0.2);
        this.creationDateCredit = creationDateCredit;
    }

    // Constructor with primary and secondary owners
    public CreditCard(Money balance, User primaryOwner, User secondaryOwner, Money creditLimit, BigDecimal interestRateCredit, LocalDate creationDateCredit) {
        super(balance, primaryOwner, secondaryOwner);
        setCreditLimit(creditLimit);
        setInterestRateCredit(interestRateCredit);
        this.creationDateCredit = creationDateCredit;
    }

    // Constructor with primary owner
    public CreditCard(Money balance, User primaryOwner, Money creditLimit, BigDecimal interestRateCredit, LocalDate creationDateCredit) {
        super(balance, primaryOwner);
        setCreditLimit(creditLimit);
        setInterestRateCredit(interestRateCredit);
        this.creationDateCredit = creationDateCredit;
    }

    @Override
    public void setBalance(Money balance) {
        LocalDate currentDate = LocalDate.now();
        boolean isAdded = false;

        if (!firstTimeAdded) { // Check if is the first time we add the interest
            if (Period.between(currentDate, this.getCreationDateCredit()).getMonths() >= 1) { // Check if it has been a month since it was created
                log.info("It has been a month since the account was created, the interest will be added automatically");
                BigDecimal multiply = this.getBalance().getAmount().multiply(interestRateCredit);
                this.setBalance(new Money(this.getBalance().getAmount().add(multiply)));
            }
        } else { // If not, check if it has been a year since interest was added
            if (Period.between(currentDate, this.getInterestAddedDate()).getMonths() >= 1) {
                log.info("It has been a month since the interest was added, the interest will be added automatically");
                BigDecimal multiply = this.getBalance().getAmount().multiply(interestRateCredit);
                this.setBalance(new Money(this.getBalance().getAmount().add(multiply)));
                isAdded = true;
                if (isAdded) { // If we add the interest, save the date to check the next time
                    interestAddedDate = LocalDate.now();
                }
            }
        }
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

    public void setInterestRateCredit(BigDecimal interestRateCredit) {
        do {
            try {
                this.interestRateCredit = interestRateCredit;
            } catch (Exception e) {
                System.err.println("The minimum interest rate is " + LIMIT_INTEREST_RATE);
            }
        } while (interestRateCredit.compareTo(LIMIT_INTEREST_RATE) == -1); // While the interest is greater than 0.5
    }
}
