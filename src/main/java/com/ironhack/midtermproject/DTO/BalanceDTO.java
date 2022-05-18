package com.ironhack.midtermproject.DTO;

import com.ironhack.midtermproject.utils.Money;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BalanceDTO {
    @NotNull(message = "You must have a balance")
    private Money balance;
}
