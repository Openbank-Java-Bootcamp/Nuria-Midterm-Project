package com.ironhack.midtermproject.service.impl.account;

import com.ironhack.midtermproject.model.account.Savings;
import com.ironhack.midtermproject.repository.account.SavingsRepository;
import com.ironhack.midtermproject.service.interfaces.account.SavingsServiceInterface;
import com.ironhack.midtermproject.utils.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
public class SavingsService implements SavingsServiceInterface {
    @Autowired
    private SavingsRepository savingsRepository;

    public Savings saveSavings(Savings savings) {
        log.info("Saving a new savings account {} inside of the database", savings.getAccountId());
        if (savings.getAccountId() != null) {
            Optional<Savings> optionalSavings = savingsRepository.findById(savings.getAccountId());
            if (optionalSavings.isPresent())
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account with id " + savings.getAccountId() + " already exist");
        }
        return savingsRepository.save(savings);
    }

    public Savings getSavings(Long id) {
        log.info("Fetching savings account {}", id);
        return savingsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Savings account not found"));
    }

    public void updateSavings(Long id, Savings savings) {
        log.info("Updating savings account {}", id);
        Savings savingsFromDB = savingsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Savings account not found"));
        savings.setAccountId(savingsFromDB.getAccountId());
        savingsRepository.save(savings);
    }

    public void deleteSavings(Long id) {
        log.info("Deleting checking account {}", id);
        savingsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Savings account not found"));
        if (savingsRepository.findById(id).isPresent()) {
            savingsRepository.deleteById(id);
        }
    }

    public void updateBalance(Long id, Money balance) {
        log.info("Updating balance of savings account {}", id);
        Savings savingsFromDB = savingsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Savings account is not found"));
        savingsFromDB.setBalance(balance);
        savingsRepository.save(savingsFromDB);
    }
}
