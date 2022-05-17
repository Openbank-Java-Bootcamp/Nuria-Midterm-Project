package com.ironhack.midtermproject.DTO;

import com.ironhack.midtermproject.utils.Money;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BalanceDTO {
    @NotEmpty(message = "You must have a balance")
    private Money balance;
}
