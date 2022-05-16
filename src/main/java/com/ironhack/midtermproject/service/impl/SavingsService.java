package com.ironhack.midtermproject.service.impl;

import com.ironhack.midtermproject.model.account.Savings;
import com.ironhack.midtermproject.repository.SavingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
public class SavingsService {
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
        log.info("Fetching a savings checking account");
        return savingsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Savings account not found"));
    }

    public void updateSavings(Long id, Savings savings) {
        Savings savingsFromDB = savingsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Savings account not found"));
        savings.setAccountId(savingsFromDB.getAccountId());
        savingsRepository.save(savings);
    }

    public void deleteSavings(Long id) {
        savingsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Savings account not found"));
        if (savingsRepository.findById(id).isPresent()) {
            savingsRepository.deleteById(id);
        }
    }
}
