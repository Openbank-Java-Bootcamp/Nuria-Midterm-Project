package com.ironhack.midtermproject.DTO;

import com.ironhack.midtermproject.utils.Money;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckingDTO {
    private Long secretKey;
    private Money balance;
    private Long primaryOwner;
    private Long secondaryOwner;
    private Money minimumBalanceChecking;
    private Money monthlyMaintenanceFeeChecking;
}
