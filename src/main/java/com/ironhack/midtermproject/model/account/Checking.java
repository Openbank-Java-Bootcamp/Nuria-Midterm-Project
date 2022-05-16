package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.utils.Money;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "checking")
@PrimaryKeyJoinColumn(name = "id_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Checking extends Account {
    @Column(name = "secret_key")
    private Long secretKeyChecking;

    @Column(name = "minimum_balance")
    private Money minimumBalanceChecking;

    @Column(name = "monthly_maintenance_fee")
    private Money monthlyMaintenanceFeeChecking;

    @Column(name = "creation_date")
    private Date creationDateChecking;

    @Enumerated(EnumType.STRING)
    private Status statusChecking;
}
