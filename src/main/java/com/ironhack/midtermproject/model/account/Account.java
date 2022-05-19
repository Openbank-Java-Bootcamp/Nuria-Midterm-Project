package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.utils.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public abstract class Account {
    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    @Column(name = "secret_key")
    @NotNull(message = "You must have a secret key")
    private Long secretKey;
    @Column(name = "creation_date")
    private LocalDate creationDate;
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount"))
    })
    @Embedded
    @NotNull(message = "You must have a balance")
    private Money balance;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "primary_owner")
    @NotNull(message = "You must have a primary owner")
    private User primaryOwner;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "secondary_owner")
    private User secondaryOwner;

    @Column(name = "penalty_fee")
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "penalty_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "penalty_amount"))
    })
    @Embedded
    private Money penaltyFee;

    // Constructor with primary and secondary owners
    public Account(Money balance, User primaryOwner, User secondaryOwner, Long secretKey) {
        this.secretKey = secretKey;
        this.creationDate = LocalDate.now();
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.penaltyFee = new Money(new BigDecimal(40));
    }

    // Constructor with primary owner
    public Account(Money balance, User primaryOwner, Long secretKey) {
        this.secretKey = secretKey;
        this.creationDate = LocalDate.now();
        //this.creationDate = LocalDate.of(2022, 03, 12);
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.penaltyFee = new Money(new BigDecimal(40));
    }

    public void deductPenaltyFee(){ // Deduct the penalty when account is below the minimum balance
        log.info("The account is below the minimum balance, the penalty (40) is will be deducted from the balance automatically");
        this.balance.setAmount(this.balance.decreaseAmount(this.getPenaltyFee().getAmount()));
    }

    public void increaseBalance(BigDecimal amount) { // Increase the balance when transferring money
        this.balance.setAmount(this.balance.increaseAmount(amount));
    }

    public void decreaseBalance(BigDecimal amount) { // Decrease the balance when transferring money
        this.balance.setAmount(this.balance.decreaseAmount(amount));
    }

    public void updateBalance(BigDecimal balance) {
        this.balance.setAmount(balance);
    }
}
