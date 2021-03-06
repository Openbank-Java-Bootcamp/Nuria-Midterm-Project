package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.utils.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "checking")
@PrimaryKeyJoinColumn(name = "id_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Checking extends Account {
    @Column(name = "minimum_balance")
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount"))
    })
    @Embedded
    @NotNull(message = "You must have a minimum balance")
    private Money minimumBalanceChecking;
    @Column(name = "monthly_maintenance_fee")
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "maintenance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "maintenance_amount"))
    })
    @Embedded
    @NotNull(message = "You must have a maintenanceFee")
    private Money monthlyMaintenanceFeeChecking;
    @Enumerated(EnumType.STRING)
    private Status statusChecking;

    // Constructor with primary, secondary owners and default values
    public Checking(Long secretKey, Money balance, User primaryOwner, User secondaryOwner) {
        super(secretKey, balance, primaryOwner, secondaryOwner);
        this.minimumBalanceChecking = new Money(new BigDecimal(250));
        this.monthlyMaintenanceFeeChecking = new Money(new BigDecimal(12));
        this.statusChecking = Status.ACTIVE;
    }

    // Constructor with primary owner and default values
    public Checking(Long secretKey, Money balance, User primaryOwner) {
        super(secretKey, balance, primaryOwner);
        this.minimumBalanceChecking = new Money(new BigDecimal(250));
        this.monthlyMaintenanceFeeChecking = new Money(new BigDecimal(12));
        this.statusChecking = Status.ACTIVE;
    }

    // Constructor with primary and secondary owners
    public Checking(Long secretKey, Money balance, User primaryOwner, User secondaryOwner, Money minimumBalanceChecking, Money monthlyMaintenanceFeeChecking) {
        super(secretKey, balance, primaryOwner, secondaryOwner);
        this.minimumBalanceChecking = minimumBalanceChecking;
        this.monthlyMaintenanceFeeChecking = monthlyMaintenanceFeeChecking;
        this.statusChecking = Status.ACTIVE;
    }

    // Constructor with primary owner
    public Checking(Long secretKey, Money balance, User primaryOwner, Money minimumBalanceChecking, Money monthlyMaintenanceFeeChecking) {
        super(secretKey, balance, primaryOwner);
        this.minimumBalanceChecking = minimumBalanceChecking;
        this.monthlyMaintenanceFeeChecking = monthlyMaintenanceFeeChecking;
        this.statusChecking = Status.ACTIVE;
    }

    @Override
    public void setBalance(Money balance) {
        super.setBalance(balance);
        if (this.getBalance().getAmount().compareTo(this.minimumBalanceChecking.getAmount()) == -1) { // If the account is below th minimum balance
            super.deductPenaltyFee();
        }
    }
}
