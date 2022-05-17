package com.ironhack.midtermproject.controller.interfaces.user;

import com.ironhack.midtermproject.model.user.ThirdParty;

public interface ThirdPartiControllerInterface {
    void saveThirdParty(ThirdParty thirdParty);
    ThirdParty getThirdParty(Long id);
    void updateThirdParty(Long id, ThirdParty thirdParty);
    void deleteThirdParty(Long id);
}
