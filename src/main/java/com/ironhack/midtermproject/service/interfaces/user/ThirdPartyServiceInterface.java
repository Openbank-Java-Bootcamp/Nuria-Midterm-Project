package com.ironhack.midtermproject.service.interfaces.user;

import com.ironhack.midtermproject.model.user.ThirdParty;

public interface ThirdPartyServiceInterface {
    ThirdParty saveThirdParty(ThirdParty thirdParty);
    ThirdParty getThirdParty(Long id);
    void updateThirdParty(Long id, ThirdParty thirdParty);
    void deleteThirdParty(Long id);
}
