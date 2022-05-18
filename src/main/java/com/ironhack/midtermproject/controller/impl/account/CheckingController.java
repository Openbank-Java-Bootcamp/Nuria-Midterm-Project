package com.ironhack.midtermproject.controller.impl.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.DTO.CheckingDTO;
import com.ironhack.midtermproject.controller.interfaces.account.CheckingControllerInterface;
import com.ironhack.midtermproject.model.account.Checking;
import com.ironhack.midtermproject.model.account.StudentChecking;
import com.ironhack.midtermproject.model.user.AccountHolder;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.service.interfaces.account.CheckingServiceInterface;
import com.ironhack.midtermproject.service.interfaces.account.StudentCheckingServiceInterface;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@RestController
@RequestMapping("/api/checking")
@Slf4j
public class CheckingController implements CheckingControllerInterface {
    @Autowired
    private CheckingServiceInterface checkingService;
    @Autowired
    private StudentCheckingServiceInterface studentCheckingService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveChecking(@RequestBody(required = false) @Valid CheckingDTO checkingDTO) {
        LocalDate currentDate = LocalDate.now();
        Optional<User> user = userRepository.findById(checkingDTO.getPrimaryOwner());
        AccountHolder accountHolder = (AccountHolder) user.get();
        StudentChecking studentChecking;

        if (Period.between(accountHolder.getDateOfBirth(), currentDate).getYears() < 24) { // If the user is less than 24, create a student checking account
            log.info("The user is less than 24, a student checking account will be created");
            if (checkingDTO.getSecondaryOwner() == null) { // If there is not a secondary owner
                studentChecking = new StudentChecking(checkingDTO.getBalance(), user.get(), checkingDTO.getSecretKey());
            } else {
                Optional<User> user2 = userRepository.findById(checkingDTO.getSecondaryOwner());
                studentChecking = new StudentChecking(checkingDTO.getBalance(), user.get(), user2.get(), checkingDTO.getSecretKey());
            }
            studentCheckingService.saveStudentChecking(studentChecking);
        } else {
            checkingService.saveChecking(checkingDTO);
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Checking getChecking(@PathVariable Long id) {
        return checkingService.getChecking(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateChecking(@PathVariable Long id, @RequestBody(required = false) @Valid Checking checking) {
        checkingService.updateChecking(id, checking);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChecking(@PathVariable Long id) {
        checkingService.deleteChecking(id);
    }

    @PatchMapping("/{id}/balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable Long id, @RequestBody @Valid BalanceDTO balanceDTO) {
        checkingService.updateBalance(id, balanceDTO.getBalance());
    }

    @PatchMapping("/transfer/{id}/{username}/{transfer}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(@PathVariable String username, @PathVariable Long id, @PathVariable BigDecimal transfer) {
        checkingService.transferMoney(username, id, transfer);
    }
}
