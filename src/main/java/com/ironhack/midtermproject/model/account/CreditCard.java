package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.utils.Money;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "credit_card")
@PrimaryKeyJoinColumn(name = "id_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard extends Account {
    @Column(name = "credit_limit")
    private Money creditLimit;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;
}
