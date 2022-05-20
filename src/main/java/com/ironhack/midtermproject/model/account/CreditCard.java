package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.utils.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
    @NotNull(message = "You must have a credit limit")
    private Money creditLimit;
    @Column(name = "interest_rate", precision = 32, scale = 4)
    @Digits(integer = 1, fraction = 1)
    @NotNull(message = "You must have a interest rate")
    private BigDecimal interestRateCredit;

    private LocalDate interestAddedDate;
    //private LocalDate interestAddedDate = LocalDate.of(2020, 03, 12);

    private boolean firstTimeAdded = true;

    private static final BigDecimal LIMIT_CREDIT = new BigDecimal(100000);
    private static final BigDecimal LIMIT_INTEREST_RATE = new BigDecimal(0.1);

    // Constructor with primary, secondary owners, and default values
    public CreditCard(Long secretKey, Money balance, User primaryOwner, User secondaryOwner) {
        super(secretKey, balance, primaryOwner, secondaryOwner);
        this.creditLimit = new Money(new BigDecimal(100));
        this.interestRateCredit = new BigDecimal(0.2);
    }

    // Constructor with primary and default values
    public CreditCard(Long secretKey, Money balance, User primaryOwner) {
        super(secretKey, balance, primaryOwner);
        this.creditLimit = new Money(new BigDecimal(100));
        this.interestRateCredit = new BigDecimal(0.2);
    }

    // Constructor with primary and secondary owners
    public CreditCard(Long secretKey, Money balance, User primaryOwner, User secondaryOwner, Money creditLimit, BigDecimal interestRateCredit) {
        super(secretKey, balance, primaryOwner, secondaryOwner);
        setCreditLimit(creditLimit);
        setInterestRateCredit(interestRateCredit);
    }

    // Constructor with primary owner
    public CreditCard(Long secretKey, Money balance, User primaryOwner, Money creditLimit, BigDecimal interestRateCredit) {
        super(secretKey, balance, primaryOwner);
        setCreditLimit(creditLimit);
        setInterestRateCredit(interestRateCredit);
    }

    @Override
    public void setBalance(Money balance) {
        LocalDate currentDate = LocalDate.now();
        boolean isAdded = false;
        super.updateBalance(balance.getAmount());

        if (firstTimeAdded) { // Check if is the first time we add the interest
            if (Period.between(this.getCreationDate(), currentDate).getYears() >= 1) { // If the created month is the same as the current month, even if was created 2 years ago it doesn't add
                log.info("It has been a month since the account was created, the interest will be added automatically");
                BigDecimal multiply = this.getBalance().getAmount().multiply(interestRateCredit);
                super.increaseBalance(multiply);
                firstTimeAdded = false;
            } else if (Period.between(this.getCreationDate(), currentDate).getMonths() >= 1) {
                log.info("It has been a month since the account was created, the interest will be added automatically");
                BigDecimal multiply = this.getBalance().getAmount().multiply(interestRateCredit);
                super.increaseBalance(multiply);
                firstTimeAdded = false;
            }
        } else { // If not, check if it has been a year since interest was added
            if (Period.between(this.getInterestAddedDate(), currentDate).getYears() >= 1) {
                log.info("It has been a month since the interest was added, the interest will be added automatically");
                BigDecimal multiply = this.getBalance().getAmount().multiply(interestRateCredit);
                super.increaseBalance(multiply);
                isAdded = true;
                if (isAdded) { // If we add the interest, save the date to check the next time
                    interestAddedDate = LocalDate.now();
                }
            } else if (Period.between(this.getInterestAddedDate(), currentDate).getMonths() >= 1) {
                log.info("It has been a month since the interest was added, the interest will be added automatically");
                BigDecimal multiply = this.getBalance().getAmount().multiply(interestRateCredit);
                super.increaseBalance(multiply);
                isAdded = true;
                if (isAdded) { // If we add the interest, save the date to check the next time
                    interestAddedDate = LocalDate.now();
                }
            }
        }
    }

    public void setCreditLimit(Money creditLimit) {
        if (creditLimit.getAmount().compareTo(LIMIT_CREDIT) == 1) { // If the limit credit is greater than the max limit
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit limit should be less than " + LIMIT_CREDIT);
        } else {
            this.creditLimit = creditLimit;
        }
    }

    public void setInterestRateCredit(BigDecimal interestRateCredit) {
        if (interestRateCredit.compareTo(LIMIT_INTEREST_RATE) == -1) { // If the interest is greater than min limit
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Interest rate should be greater than " + LIMIT_INTEREST_RATE);
        } else {
            this.interestRateCredit = interestRateCredit;
        }
    }
}
