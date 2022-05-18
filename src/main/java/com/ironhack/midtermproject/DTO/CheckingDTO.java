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
    @NotNull(message = "You must have a secret key")
    private Long secretKey;
    @NotNull(message = "You must have a balance")
    private Money balance;
    @NotNull(message = "You must have a primary owner")
    private Long primaryOwner;
    private Long secondaryOwner;
    private Money minimumBalanceChecking;
    private Money monthlyMaintenanceFeeChecking;
}
