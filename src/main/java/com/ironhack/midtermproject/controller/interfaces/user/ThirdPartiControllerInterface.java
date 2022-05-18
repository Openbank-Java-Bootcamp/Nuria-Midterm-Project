package com.ironhack.midtermproject.controller.interfaces.user;

import com.ironhack.midtermproject.model.user.ThirdParty;

import java.math.BigDecimal;

public interface ThirdPartiControllerInterface {
    void saveThirdParty(ThirdParty thirdParty);
    ThirdParty getThirdParty(Long id);
    void updateThirdParty(Long id, ThirdParty thirdParty);
    void deleteThirdParty(Long id);
    void transferMoney(String hashedKey, BigDecimal amount, Long id, Long secretKey);
    void receiveMoney(String hashedKey, BigDecimal amount, Long id, Long secretKey);
}
