package com.ironhack.midtermproject.service.impl.account;

import com.ironhack.midtermproject.DTO.StudentCheckingDTO;
import com.ironhack.midtermproject.model.account.Account;
import com.ironhack.midtermproject.model.account.Checking;
import com.ironhack.midtermproject.model.account.StudentChecking;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.repository.account.AccountRepository;
import com.ironhack.midtermproject.repository.account.StudentCheckingRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.service.interfaces.account.StudentCheckingServiceInterface;
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
public class StudentCheckingService implements StudentCheckingServiceInterface {
    @Autowired
    private StudentCheckingRepository studentCheckingRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    public StudentChecking saveStudentChecking(StudentChecking studentChecking) {
        log.info("Saving a new student checking account inside of the database");
        if (studentChecking.getAccountId() != null) {
            Optional<StudentChecking> optionalStudentChecking = studentCheckingRepository.findById(studentChecking.getAccountId());
            if (optionalStudentChecking.isPresent())
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account with id " + studentChecking.getAccountId() + " already exist");
        }
        return studentCheckingRepository.save(studentChecking);
    }

    public StudentChecking getStudentChecking(Long id) {
        log.info("Fetching student checking account {}", id);
        return studentCheckingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student checking account not found"));
    }

    public Money getStudentBalance(Long id, String username) {
        log.info("Fetching checking account balance {}", id);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String thisUsername;
        if (principal instanceof UserDetails) {
            thisUsername = ((UserDetails) principal).getUsername();
        } else {
            thisUsername = principal.toString();
        }

        if (thisUsername.equals(username)) {
            studentCheckingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student checking account not found"));
        }
        return accountRepository.findByIdAndUsername(id, username);
    }

    public void updateStudentChecking(Long id, StudentCheckingDTO studentCheckingDTO) {
        log.info("Updating student checking account {}", id);
        StudentChecking studentChecking;
        if (studentCheckingDTO.getSecondaryOwner() == null) {
            Optional<User> user = userRepository.findById(studentCheckingDTO.getPrimaryOwner());
            studentChecking = new StudentChecking(studentCheckingDTO.getSecretKey(), studentCheckingDTO.getBalance(), user.get());
        } else {
            Optional<User> user1 = userRepository.findById(studentCheckingDTO.getPrimaryOwner());
            Optional<User> user2 = userRepository.findById(studentCheckingDTO.getSecondaryOwner());
            studentChecking = new StudentChecking(studentCheckingDTO.getSecretKey(), studentCheckingDTO.getBalance(), user1.get(), user2.get());
        }
        StudentChecking studentCheckingFromDB = studentCheckingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student checking account not found"));
        studentChecking.setAccountId(studentCheckingFromDB.getAccountId());
        studentCheckingRepository.save(studentChecking);
    }

    public void deleteStudentChecking(Long id) {
        log.info("Deleting student checking account {}", id);
        studentCheckingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student checking account not found"));
        if (studentCheckingRepository.findById(id).isPresent()) {
            studentCheckingRepository.deleteById(id);
        }
    }

    public void updateBalance(String username, Long id, Money balance) {
        log.info("Updating balance of student checking account {}", id);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String thisUsername;
        if (principal instanceof UserDetails) {
            thisUsername = ((UserDetails) principal).getUsername();
        } else {
            thisUsername = principal.toString();
        }

        if (thisUsername.equals(username)) {
            StudentChecking studentCheckingFromDB = studentCheckingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student checking account is not found"));
            studentCheckingFromDB.setBalance(balance);
            studentCheckingRepository.save(studentCheckingFromDB);
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
            StudentChecking thisStudentChecking = (StudentChecking) accountRepository.findById(idSend).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student checking account is not found"));
            Account studentCheckingReceiver = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student checking receiver account is not found"));

            if (thisStudentChecking.getBalance().getAmount().compareTo(transfer) == -1) { // If the transfer is greater than the account balance
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer should be lower than " + thisStudentChecking.getBalance().getAmount());
            } else {
                thisStudentChecking.decreaseBalance(transfer); // Decrease the amount in the user account
                studentCheckingReceiver.increaseBalance(transfer); // Increase the amount in the receiver account
                studentCheckingRepository.save(thisStudentChecking);
                accountRepository.save(studentCheckingReceiver);
            }
        }
    }
}
