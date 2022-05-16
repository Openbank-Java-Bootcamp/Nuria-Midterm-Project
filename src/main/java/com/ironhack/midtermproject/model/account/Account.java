package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.utils.Money;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount"))
    })
    @Embedded
    private Money balance;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "primary_owner")
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "primary_user")),
            @AttributeOverride(name = "name", column = @Column(name = "primary_name")),
            @AttributeOverride(name = "username", column = @Column(name = "primary_username")),
            @AttributeOverride(name = "password", column = @Column(name = "primary_password"))
    })
    @Embedded
    private User primaryOwner;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "secondary_owner")
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "secondary_user")),
            @AttributeOverride(name = "name", column = @Column(name = "secondary_name")),
            @AttributeOverride(name = "username", column = @Column(name = "secondary_username")),
            @AttributeOverride(name = "password", column = @Column(name = "secondary_password"))
    })
    @Embedded
    private User secondaryOwner;

    @Column(name = "penalty_fee")
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "penalty_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "penalty_amount"))
    })
    @Embedded
    private Money penaltyFee;

    public Account(Money balance, User primaryOwner, User secondaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.penaltyFee = new Money(new BigDecimal(40));
    }

    public Account(Money balance, User primaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.penaltyFee = new Money(new BigDecimal(40));
    }

    public void deductPenaltyFee(){
        this.setBalance(new Money(this.getBalance().getAmount().subtract(this.getPenaltyFee().getAmount())));
    }
}
