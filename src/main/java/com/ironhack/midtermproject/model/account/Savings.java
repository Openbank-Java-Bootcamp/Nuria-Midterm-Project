package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.utils.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

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
    @Column(name = "secret_key")
    @NotEmpty(message = "You must have a secret key")
    private Long secretKeySavings;
    @Column(name = "minimum_balance")
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount"))
    })
    @Embedded
    @NotEmpty(message = "You must have a minimum balance")
    private Money minimumBalanceSavings;
    @Column(name = "creation_date")
    private LocalDate creationDateSavings;
    @Column(name = "interest_rate")
    @NotEmpty(message = "You must have a interest rate")
    private BigDecimal interestRateSavings;
    @Enumerated(EnumType.STRING)
    private Status statusSavings;

    private LocalDate interestAddedDate;

    private static final BigDecimal LIMIT_INTEREST_RATE = new BigDecimal(0.5);
    private static final BigDecimal LIMIT_MINIMUM_BALANCE = new BigDecimal(100);

    public Savings(Money balance, User primaryOwner, User secondaryOwner, Long secretKeySavings, LocalDate creationDateSavings) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKeySavings = secretKeySavings;
        this.minimumBalanceSavings = new Money(new BigDecimal(1000));
        this.creationDateSavings = creationDateSavings;
        this.interestRateSavings = new BigDecimal(0.0025);
        this.statusSavings = Status.ACTIVE;
    }

    public Savings(Money balance, User primaryOwner, Long secretKeySavings, LocalDate creationDateSavings) {
        super(balance, primaryOwner);
        this.secretKeySavings = secretKeySavings;
        this.minimumBalanceSavings = new Money(new BigDecimal(1000));
        this.creationDateSavings = creationDateSavings;
        this.interestRateSavings = new BigDecimal(0.0025);
        this.statusSavings = Status.ACTIVE;
    }

    public Savings(Money balance, User primaryOwner, User secondaryOwner, Long secretKeySavings, Money minimumBalanceSavings, LocalDate creationDateSavings, BigDecimal interestRateSavings) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKeySavings = secretKeySavings;
        setMinimumBalanceSavings(minimumBalanceSavings);
        this.creationDateSavings = creationDateSavings;
        setInterestRateSavings(interestRateSavings);
        this.statusSavings = Status.ACTIVE;
    }

    public Savings(Money balance, User primaryOwner, Long secretKeySavings, Money minimumBalanceSavings, LocalDate creationDateSavings, BigDecimal interestRateSavings) {
        super(balance, primaryOwner);
        this.secretKeySavings = secretKeySavings;
        setMinimumBalanceSavings(minimumBalanceSavings);
        this.creationDateSavings = creationDateSavings;
        setInterestRateSavings(interestRateSavings);
        this.statusSavings = Status.ACTIVE;
    }

    @Override
    public void setBalance(Money balance) {
        LocalDate currentDate = LocalDate.now();
        boolean isAdded = false;

        if (Period.between(currentDate, this.getCreationDateSavings()).getYears() >= 1 || Period.between(currentDate, this.getInterestAddedDate()).getYears() >= 1) { // If it has been a year since the account was created or since interest was added, add the interest
            log.info("It has been a year since the account was created or since the interest was added, the interest will be added automatically");
            BigDecimal multiply = this.getBalance().getAmount().multiply(interestRateSavings);
            this.setBalance(new Money(this.getBalance().getAmount().add(multiply)));
            isAdded = true;
            if (isAdded) {
                interestAddedDate = LocalDate.now();
            }
        }

        if (this.getBalance().getAmount().compareTo(minimumBalanceSavings.getAmount()) == -1) { // If the account is below th minimum balance
            this.deductPenaltyFee();
        } else {
            this.setBalance(balance);
        }
    }

    public void setInterestRateSavings(BigDecimal interestRateSavings) {
        do {
            try {
                this.interestRateSavings = interestRateSavings;
            } catch (Exception e) {
                System.err.println("The maximum interest rate is " + LIMIT_INTEREST_RATE);
            }
        } while (interestRateSavings.compareTo(LIMIT_INTEREST_RATE) == 1); // While the interest is greater than limit
    }

    public void setMinimumBalanceSavings(Money minimumBalanceSavings) {
        do {
            try {
                this.minimumBalanceSavings = minimumBalanceSavings;
            } catch (Exception e) {
                System.err.println("The minimum limit of minimum balance is " + LIMIT_MINIMUM_BALANCE);
            }
        } while (minimumBalanceSavings.getAmount().compareTo(LIMIT_MINIMUM_BALANCE) == -1); // While the minimum balance is less than limit
    }
}
