package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.enums.Status;
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
@Table(name = "savings")
@PrimaryKeyJoinColumn(name = "id_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Savings extends Account {
    @Column(name = "minimum_balance")
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount"))
    })
    @Embedded
    @NotNull(message = "You must have a minimum balance")
    private Money minimumBalanceSavings;
    @Column(name = "interest_rate")
    @NotNull(message = "You must have a interest rate")
    @Digits(fraction = 1, integer = 4)
    private BigDecimal interestRateSavings;
    @Enumerated(EnumType.STRING)
    private Status statusSavings;

    private LocalDate interestAddedDate;
    //private LocalDate interestAddedDate = LocalDate.of(2012, 05, 12);

    private boolean firstTimeAdded = true;

    private static final BigDecimal LIMIT_INTEREST_RATE = new BigDecimal(0.5);
    private static final BigDecimal LIMIT_MINIMUM_BALANCE = new BigDecimal(100);

    // Constructor with primary, secondary owners, and default values
    public Savings(Money balance, User primaryOwner, User secondaryOwner, Long secretKey) {
        super(balance, primaryOwner, secondaryOwner, secretKey);
        this.minimumBalanceSavings = new Money(new BigDecimal(1000));
        this.interestRateSavings = new BigDecimal(0.0025);
        this.statusSavings = Status.ACTIVE;
    }

    // Constructor with primary owner and default values
    public Savings(Money balance, User primaryOwner, Long secretKey) {
        super(balance, primaryOwner, secretKey);
        this.minimumBalanceSavings = new Money(new BigDecimal(1000));
        this.interestRateSavings = new BigDecimal(0.0025);
        this.statusSavings = Status.ACTIVE;
    }

    // Constructor with primary and secondary owners
    public Savings(Money balance, User primaryOwner, User secondaryOwner, Long secretKey, Money minimumBalanceSavings, BigDecimal interestRateSavings) {
        super(balance, primaryOwner, secondaryOwner, secretKey);
        setMinimumBalanceSavings(minimumBalanceSavings);
        setInterestRateSavings(interestRateSavings);
        this.statusSavings = Status.ACTIVE;
    }

    // Constructor with primary owner
    public Savings(Money balance, User primaryOwner, Long secretKey, Money minimumBalanceSavings, BigDecimal interestRateSavings) {
        super(balance, primaryOwner, secretKey);
        setMinimumBalanceSavings(minimumBalanceSavings);
        setInterestRateSavings(interestRateSavings);
        this.statusSavings = Status.ACTIVE;
    }

    @Override
    public void setBalance(Money balance) {
        LocalDate currentDate = LocalDate.now();
        boolean isAdded = false;
        super.updateBalance(balance.getAmount());

        if (this.getBalance().getAmount().compareTo(minimumBalanceSavings.getAmount()) == -1) { // If the account is below th minimum balance
            super.deductPenaltyFee();
        }
        if (firstTimeAdded) { // Check if is the first time we add the interest
            if (Period.between(this.getCreationDate(), currentDate).getYears() >= 1) { // Check if it has been a year since it was created
                log.info("It has been a year since the account was created, the interest will be added automatically");
                BigDecimal multiply = this.getBalance().getAmount().multiply(interestRateSavings);
                super.increaseBalance(multiply);
                firstTimeAdded = false;
            }
        } else { // If not, check if it has been a year since interest was added
            if (Period.between(this.getInterestAddedDate(), currentDate).getYears() >= 1) {
                log.info("It has been a year since the interest was added, the interest will be added automatically");
                BigDecimal multiply = this.getBalance().getAmount().multiply(interestRateSavings);
                super.increaseBalance(multiply);
                isAdded = true;
                if (isAdded) { // If we add the interest, save the date to check the next time
                    interestAddedDate = LocalDate.now();
                }
            }
        }
    }

    public void setInterestRateSavings(BigDecimal interestRateSavings) {
        if (interestRateSavings.compareTo(LIMIT_INTEREST_RATE) == 1) { // If the interest is greater than limit
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Interest rate should be less than " + LIMIT_INTEREST_RATE);
        } else {
            this.interestRateSavings = interestRateSavings;
        }
    }

    public void setMinimumBalanceSavings(Money minimumBalanceSavings) {
        if (minimumBalanceSavings.getAmount().compareTo(LIMIT_MINIMUM_BALANCE) == -1) { // If the minimum balance is less than limit
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum balance should be greater than " + LIMIT_MINIMUM_BALANCE);
        } else {
            this.minimumBalanceSavings = minimumBalanceSavings;
        }
    }
}
