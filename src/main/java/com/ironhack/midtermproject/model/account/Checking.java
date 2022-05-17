package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.utils.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "checking")
@PrimaryKeyJoinColumn(name = "id_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Checking extends Account {
    @Column(name = "secret_key")
    @NotEmpty(message = "You must have a secret key")
    private Long secretKeyChecking;
    @Column(name = "minimum_balance")
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount"))
    })
    @Embedded
    @NotEmpty(message = "You must have a minimum balance")
    private Money minimumBalanceChecking;
    @Column(name = "monthly_maintenance_fee")
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "maintenance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "maintenance_amount"))
    })
    @Embedded
    @NotEmpty(message = "You must have a maintenanceFee")
    private Money monthlyMaintenanceFeeChecking;
    @Column(name = "creation_date")
    @NotEmpty(message = "You must have a creation date")
    private Date creationDateChecking;
    @Enumerated(EnumType.STRING)
    private Status statusChecking;

    public Checking(Money balance, User primaryOwner, User secondaryOwner, Long secretKeyChecking, Date creationDateChecking) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKeyChecking = secretKeyChecking;
        this.minimumBalanceChecking = new Money(new BigDecimal(250));
        this.monthlyMaintenanceFeeChecking = new Money(new BigDecimal(12));
        this.creationDateChecking = creationDateChecking;
        this.statusChecking = Status.ACTIVE;
    }

    public Checking(Money balance, User primaryOwner, Long secretKeyChecking, Date creationDateChecking) {
        super(balance, primaryOwner);
        this.secretKeyChecking = secretKeyChecking;
        this.minimumBalanceChecking = new Money(new BigDecimal(250));
        this.monthlyMaintenanceFeeChecking = new Money(new BigDecimal(12));
        this.creationDateChecking = creationDateChecking;
        this.statusChecking = Status.ACTIVE;
    }

    @Override
    public void setBalance(Money balance) {
        if (this.getBalance().getAmount().compareTo(minimumBalanceChecking.getAmount()) == -1) { // If the account is below th minimum balance
            this.deductPenaltyFee();
        } else {
            this.setBalance(balance);
        }
    }
}
