package com.ironhack.midtermproject.service.impl.user;

import com.google.common.hash.Hashing;
import com.ironhack.midtermproject.DTO.ThirdPartyDTO;
import com.ironhack.midtermproject.model.user.ThirdParty;
import com.ironhack.midtermproject.repository.account.AccountRepository;
import com.ironhack.midtermproject.repository.user.ThirdPartyRepository;
import com.ironhack.midtermproject.service.interfaces.user.ThirdPartyServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@Slf4j
public class ThirdPartyService implements ThirdPartyServiceInterface {
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private AccountRepository accountRepository;

    public ThirdParty saveThirdParty(ThirdPartyDTO thirdPartyDTO) {
        ThirdParty thirdParty = new ThirdParty(thirdPartyDTO.getName(), thirdPartyDTO.getHashedKey());
        log.info("Saving a new third party inside of the database");
        if (thirdParty.getId() != null) {
            Optional<ThirdParty> optionalThirdParty = thirdPartyRepository.findById(thirdParty.getId());
            if (optionalThirdParty.isPresent())
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User with id " + thirdParty.getId() + " already exist");
        }
        return thirdPartyRepository.save(thirdParty);
    }

    public ThirdParty getThirdParty(Long id) {
        log.info("Fetching third party {}", id);
        return thirdPartyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Third party user not found"));
    }

    public void updateThirdParty(Long id, ThirdParty thirdParty) {
        log.info("Updating third party {}", id);
        ThirdParty thirdPartyFromDB = thirdPartyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Third party user not found"));
        thirdParty.setId(thirdPartyFromDB.getId());
        thirdPartyRepository.save(thirdParty);
    }

    public void deleteThirdParty(Long id) {
        log.info("Deleting third party {}", id);
        thirdPartyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Third party user not found"));
        if (thirdPartyRepository.findById(id).isPresent()) {
            thirdPartyRepository.deleteById(id);
        }
    }

    public void transferMoney(String hashedKey, BigDecimal amount, Long id, Long secretKey) {
        log.info("Transferring money, {} will transfer", amount);
        String hashed = Hashing.sha256().hashString(hashedKey, StandardCharsets.UTF_8).toString();
        Optional<ThirdParty> thirdPartyFromDB = Optional.ofNullable(thirdPartyRepository.findByHashedKey(hashed).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Third party user not found")));
        String hash = String.valueOf(thirdPartyFromDB.get().getHashedKey());
        if (hash.equals(hashedKey)) { // If the hashed key is the same
            accountRepository.findByAccountIdAndSecretKey(id, secretKey).get().increaseBalance(amount); // Increase amount in the receiver account
        }
    }

    public void receiveMoney(String hashedKey, BigDecimal amount, Long id, Long secretKey) {
        log.info("Receiving money, {} will be received", amount);
        String hashed = Hashing.sha256().hashString(hashedKey, StandardCharsets.UTF_8).toString();
        Optional<ThirdParty> thirdPartyFromDB = Optional.ofNullable(thirdPartyRepository.findByHashedKey(hashed).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Third party user not found")));
        String hash = String.valueOf(thirdPartyFromDB.get().getHashedKey());
        if (hash.equals(hashedKey)) { // If the hashed key is the same
            accountRepository.findByAccountIdAndSecretKey(id, secretKey).get().decreaseBalance(amount); // Decrease amount in the receiver account
        }
    }
}
