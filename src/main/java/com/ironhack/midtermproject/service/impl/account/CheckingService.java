package com.ironhack.midtermproject.service.impl.account;

import com.ironhack.midtermproject.DTO.CheckingDTO;
import com.ironhack.midtermproject.model.account.Account;
import com.ironhack.midtermproject.model.account.Checking;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.repository.account.AccountRepository;
import com.ironhack.midtermproject.repository.account.CheckingRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.service.impl.user.UserService;
import com.ironhack.midtermproject.service.interfaces.account.CheckingServiceInterface;
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
public class CheckingService implements CheckingServiceInterface {
    @Autowired
    private CheckingRepository checkingRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    public Checking saveChecking(CheckingDTO checkingDTO) {
        Checking checking;
        if (checkingDTO.getSecondaryOwner() == null) {
            Optional<User> user = userRepository.findById(checkingDTO.getPrimaryOwner());
            checking = new Checking(checkingDTO.getSecretKey(), checkingDTO.getBalance(), user.get(), checkingDTO.getMinimumBalanceChecking(), checkingDTO.getMonthlyMaintenanceFeeChecking());
        } else {
            Optional<User> user1 = userRepository.findById(checkingDTO.getPrimaryOwner());
            Optional<User> user2 = userRepository.findById(checkingDTO.getSecondaryOwner());
            checking = new Checking(checkingDTO.getSecretKey(), checkingDTO.getBalance(), user1.get(), user2.get(), checkingDTO.getMinimumBalanceChecking(), checkingDTO.getMonthlyMaintenanceFeeChecking());
        }
        if (checking.getAccountId() != null) {
            Optional<Checking> optionalChecking = checkingRepository.findById(checking.getAccountId());
            if (optionalChecking.isPresent())
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account with id " + checking.getAccountId() + " already exist");
        }
        log.info("Saving a new checking account inside of the database");
        return checkingRepository.save(checking);
    }

    public Checking getChecking(Long id) {
        log.info("Fetching checking account {}", id);
        return checkingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking account not found"));
    }

    public Money getCheckingBalance(Long id, String username) {
        log.info("Fetching checking account balance {}", id);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String thisUsername;
        if (principal instanceof UserDetails) {
            thisUsername = ((UserDetails) principal).getUsername();
        } else {
            thisUsername = principal.toString();
        }

        if (thisUsername.equals(username)) {
            checkingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking account not found"));
        }
        return accountRepository.findByIdAndUsername(id, username);
    }

    public void updateChecking(Long id, CheckingDTO checkingDTO) {
        log.info("Updating checking account {}", id);
        Checking checking;
        if (checkingDTO.getSecondaryOwner() == null) {
            Optional<User> user = userRepository.findById(checkingDTO.getPrimaryOwner());
            checking = new Checking(checkingDTO.getSecretKey(), checkingDTO.getBalance(), user.get(), checkingDTO.getMinimumBalanceChecking(), checkingDTO.getMonthlyMaintenanceFeeChecking());
        } else {
            Optional<User> user1 = userRepository.findById(checkingDTO.getPrimaryOwner());
            Optional<User> user2 = userRepository.findById(checkingDTO.getSecondaryOwner());
            checking = new Checking(checkingDTO.getSecretKey(), checkingDTO.getBalance(), user1.get(), user2.get(), checkingDTO.getMinimumBalanceChecking(), checkingDTO.getMonthlyMaintenanceFeeChecking());
        }
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

    public void updateBalance(String username, Long id, Money balance) {
        log.info("Updating balance of account {}", id);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String thisUsername;
        if (principal instanceof UserDetails) {
            thisUsername = ((UserDetails) principal).getUsername();
        } else {
            thisUsername = principal.toString();
        }

        if (thisUsername.equals(username)) {
            Checking checkingFromDB = checkingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking account is not found"));
            checkingFromDB.setBalance(balance);
            checkingRepository.save(checkingFromDB);
        }
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
            Checking thisChecking = (Checking) accountRepository.findById(idSend).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking account is not found"));
            Account checkingReceiver = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking receiver account is not found"));

            if (thisChecking.getBalance().getAmount().compareTo(transfer) == -1) { // If the transfer is greater than the account balance
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer should be lower than " + thisChecking.getBalance().getAmount());
            } else {
                thisChecking.decreaseBalance(transfer); // Decrease the amount in the user account
                checkingReceiver.increaseBalance(transfer); // Increase the amount in the receiver account
                checkingRepository.save(thisChecking);
                accountRepository.save(checkingReceiver);
            }
        }
    }
}
