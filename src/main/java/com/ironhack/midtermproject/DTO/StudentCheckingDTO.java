package com.ironhack.midtermproject.DTO;

import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCheckingDTO {
    private Long secretKey;
    private Money balance;
    private Long primaryOwner;
    private Long secondaryOwner;

    public StudentCheckingDTO(Long secretKey, Money balance, Long primaryOwner) {
        this.secretKey = secretKey;
        this.balance = balance;
        this.primaryOwner = primaryOwner;
    }
}
