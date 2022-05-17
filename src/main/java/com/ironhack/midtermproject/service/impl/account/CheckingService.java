package com.ironhack.midtermproject.service.impl.account;

import com.ironhack.midtermproject.model.account.Checking;
import com.ironhack.midtermproject.repository.account.AccountRepository;
import com.ironhack.midtermproject.repository.account.CheckingRepository;
import com.ironhack.midtermproject.service.interfaces.account.CheckingServiceInterface;
import com.ironhack.midtermproject.utils.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
public class CheckingService implements CheckingServiceInterface {
    @Autowired
    private CheckingRepository checkingRepository;
    @Autowired
    private AccountRepository accountRepository;

    public Checking saveChecking(Checking checking) {
        log.info("Saving a new checking account {} inside of the database", checking.getAccountId());
        if (checking.getAccountId() != null) {
            Optional<Checking> optionalChecking = checkingRepository.findById(checking.getAccountId());
            if (optionalChecking.isPresent())
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account with id " + checking.getAccountId() + " already exist");
        }
        return checkingRepository.save(checking);
    }

    public Checking getChecking(Long id) {
        log.info("Fetching checking account {}", id);
        return checkingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking account not found"));
    }

    public void updateChecking(Long id, Checking checking) {
        log.info("Updating checking account {}", id);
        Checking checkingFromDB = checkingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking account not found"));
        checking.setAccountId(checkingFromDB.getAccountId());
        checkingRepository.save(checking);
    }

    public void deleteChecking(Long id) {
        log.info("Deleting checking account {}", id);
        checkingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking account not found"));
        if (checkingRepository.findById(id).isPresent()) {
            checkingRepository.deleteById(id);
        }
    }

    public void updateBalance(Long id, Money balance) {
        log.info("Updating balance of account {}", id);
        Checking checkingFromDB = checkingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking account is not found"));
        checkingFromDB.setBalance(balance);
        checkingRepository.save(checkingFromDB);
    }

    public void transferMoney(String name, Long id, Money transfer) {
        log.info("Transferring money");
        Checking checkingReceiver = (Checking) accountRepository.findByNameAndAccountId(name, id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking account is not found"));

        // do while transfer > checkingSed.getBalance
        //checkingSend.decreaseBalance(transfer);
        checkingReceiver.increaseBalance(transfer);
    }
}
