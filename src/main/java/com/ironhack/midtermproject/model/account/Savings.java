package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.enums.Status;
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
    private Money minimumBalanceSavings;

    @Column(name = "creation_date")
    private Date creationDateSavings;

    @Column(name = "interest_rate")
    private BigDecimal interestRateSavings;

    @Enumerated(EnumType.STRING)
    private Status statusSavings;
}
