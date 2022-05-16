package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.utils.Money;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "savings")
@PrimaryKeyJoinColumn(name = "id_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Savings extends Account {
    @Column(name = "secret_key")
    private Long secretKeySavings;
    @Column(name = "minimum_balance")
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount"))
    })
    @Embedded
    private Money minimumBalanceSavings;
    @Column(name = "creation_date")
    private Date creationDateSavings;
    @Column(name = "interest_rate")
    private BigDecimal interestRateSavings;
    @Enumerated(EnumType.STRING)
    private Status statusSavings;

    private static final BigDecimal LIMIT_INTEREST_RATE = new BigDecimal(0.5);
    private static final BigDecimal LIMIT_MINIMUM_BALANCE = new BigDecimal(100);

    public Savings(Money balance, User primaryOwner, User secondaryOwner, Long secretKeySavings, Date creationDateSavings) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKeySavings = secretKeySavings;
        this.minimumBalanceSavings = new Money(new BigDecimal(1000));
        this.creationDateSavings = creationDateSavings;
        this.interestRateSavings = new BigDecimal(0.0025);
        this.statusSavings = Status.ACTIVE;
    }

    public Savings(Money balance, User primaryOwner, Long secretKeySavings, Date creationDateSavings) {
        super(balance, primaryOwner);
        this.secretKeySavings = secretKeySavings;
        this.minimumBalanceSavings = new Money(new BigDecimal(1000));
        this.creationDateSavings = creationDateSavings;
        this.interestRateSavings = new BigDecimal(0.0025);
        this.statusSavings = Status.ACTIVE;
    }

    public Savings(Money balance, User primaryOwner, User secondaryOwner, Long secretKeySavings, Money minimumBalanceSavings, Date creationDateSavings, BigDecimal interestRateSavings) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKeySavings = secretKeySavings;
        setMinimumBalanceSavings(minimumBalanceSavings);
        this.creationDateSavings = creationDateSavings;
        setInterestRateSavings(interestRateSavings);
        this.statusSavings = Status.ACTIVE;
    }

    public Savings(Money balance, User primaryOwner, Long secretKeySavings, Money minimumBalanceSavings, Date creationDateSavings, BigDecimal interestRateSavings) {
        super(balance, primaryOwner);
        this.secretKeySavings = secretKeySavings;
        setMinimumBalanceSavings(minimumBalanceSavings);
        this.creationDateSavings = creationDateSavings;
        setInterestRateSavings(interestRateSavings);
        this.statusSavings = Status.ACTIVE;
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
