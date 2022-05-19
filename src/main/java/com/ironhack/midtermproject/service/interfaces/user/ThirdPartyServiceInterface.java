package com.ironhack.midtermproject.service.interfaces.user;

import com.ironhack.midtermproject.DTO.ThirdPartyDTO;
import com.ironhack.midtermproject.model.user.ThirdParty;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;

public interface ThirdPartyServiceInterface {
    ThirdParty saveThirdParty(ThirdPartyDTO thirdPartyDTO);
    ThirdParty getThirdParty(Long id);
    void updateThirdParty(Long id, ThirdParty thirdParty);
    void deleteThirdParty(Long id);
    void transferMoney(String hashedKey, BigDecimal amount, Long id, Long secretKey);
    void receiveMoney(String hashedKey, BigDecimal amount, Long id, Long secretKey);
}
