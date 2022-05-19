package com.ironhack.midtermproject.service.impl.account;

import com.ironhack.midtermproject.DTO.SavingsDTO;
import com.ironhack.midtermproject.model.account.Account;
import com.ironhack.midtermproject.model.account.Savings;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.repository.account.AccountRepository;
import com.ironhack.midtermproject.repository.account.SavingsRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.service.interfaces.account.SavingsServiceInterface;
import com.ironhack.midtermproject.utils.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
public class SavingsService implements SavingsServiceInterface {
    @Autowired
    private SavingsRepository savingsRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    public Savings saveSavings(SavingsDTO savingsDTO) {
        Savings savings;
        if (savingsDTO.getSecondaryOwner() == null) {
            Optional<User> user = userRepository.findById(savingsDTO.getPrimaryOwner());
            savings = new Savings(savingsDTO.getBalance(), user.get(), savingsDTO.getSecretKey(), savingsDTO.getMinimumBalanceSavings(), savingsDTO.getInterestRateSavings());
        } else {
            Optional<User> user1 = userRepository.findById(savingsDTO.getPrimaryOwner());
            Optional<User> user2 = userRepository.findById(savingsDTO.getSecondaryOwner());
            savings = new Savings(savingsDTO.getBalance(), user1.get(), user2.get(), savingsDTO.getSecretKey(), savingsDTO.getMinimumBalanceSavings(), savingsDTO.getInterestRateSavings());
        }
        log.info("Saving a new savings account inside of the database");
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

    public Money getSavingsBalance(Long id, String username) {
        log.info("Fetching checking account balance {}", id);
        savingsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Savings account not found"));
        return accountRepository.findByIdAndUsername(id, username);
    }

    public void updateSavings(Long id, SavingsDTO savingsDTO) {
        log.info("Updating savings account {}", id);
        Savings savings;
        if (savingsDTO.getSecondaryOwner() == null) {
            Optional<User> user = userRepository.findById(savingsDTO.getPrimaryOwner());
            savings = new Savings(savingsDTO.getBalance(), user.get(), savingsDTO.getSecretKey(), savingsDTO.getMinimumBalanceSavings(), savingsDTO.getInterestRateSavings());
        } else {
            Optional<User> user1 = userRepository.findById(savingsDTO.getPrimaryOwner());
            Optional<User> user2 = userRepository.findById(savingsDTO.getSecondaryOwner());
            savings = new Savings(savingsDTO.getBalance(), user1.get(), user2.get(), savingsDTO.getSecretKey(), savingsDTO.getMinimumBalanceSavings(), savingsDTO.getInterestRateSavings());
        }

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

    public void transferMoney(String username, Long id, BigDecimal transfer) {
        log.info("Transferring money, {} will transfer", transfer);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String thisUsername;
        if (principal instanceof UserDetails) {
            thisUsername = ((UserDetails) principal).getUsername();
        } else {
            thisUsername = principal.toString();
        }

        if (thisUsername.equals(username)) {
            Long idSend = accountRepository.findByUsername(username);
            Savings thisSavings = (Savings) accountRepository.findById(idSend).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Savings account is not found"));
            Account savingsReceiver = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Savings receiver account is not found"));

            if (thisSavings.getBalance().getAmount().compareTo(transfer) == -1) { // If the transfer is greater than the account balance
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer should be lower than " + thisSavings.getBalance().getAmount());
            } else {
                thisSavings.decreaseBalance(transfer); // Decrease the amount in the user account
                savingsReceiver.increaseBalance(transfer); // Increase the amount in the receiver account
                savingsRepository.save(thisSavings);
                accountRepository.save(savingsReceiver);
            }
        }
    }
}
