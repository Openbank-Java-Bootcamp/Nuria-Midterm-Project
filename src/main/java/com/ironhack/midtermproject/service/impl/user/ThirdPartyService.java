package com.ironhack.midtermproject.service.impl.user;

import com.ironhack.midtermproject.model.user.ThirdParty;
import com.ironhack.midtermproject.repository.user.ThirdPartyRepository;
import com.ironhack.midtermproject.service.interfaces.user.ThirdPartyServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
public class ThirdPartyService implements ThirdPartyServiceInterface {
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    public ThirdParty saveThirdParty(ThirdParty thirdParty) {
        log.info("Saving a new third party {} inside of the database", thirdParty.getId());
        if (thirdParty.getId() != null) {
            Optional<ThirdParty> optionalThirdParty = thirdPartyRepository.findById(thirdParty.getId());
            if (optionalThirdParty.isPresent())
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User with id " + thirdParty.getId() + " already exist");
        }
        return thirdPartyRepository.save(thirdParty);
    }

    public ThirdParty getThirdParty(Long id) {
        log.info("Fetching a third party");
        return thirdPartyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Third party user not found"));
    }

    public void updateThirdParty(Long id, ThirdParty thirdParty) {
        ThirdParty thirdPartyFromDB = thirdPartyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Third party user not found"));
        thirdParty.setId(thirdPartyFromDB.getId());
        thirdPartyRepository.save(thirdParty);
    }

    public void deleteThirdParty(Long id) {
        thirdPartyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Third party user not found"));
        if (thirdPartyRepository.findById(id).isPresent()) {
            thirdPartyRepository.deleteById(id);
        }
    }
}
