package com.ironhack.midtermproject.controller.impl.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.controller.interfaces.account.CheckingControllerInterface;
import com.ironhack.midtermproject.model.account.Checking;
import com.ironhack.midtermproject.model.account.StudentChecking;
import com.ironhack.midtermproject.model.user.AccountHolder;
import com.ironhack.midtermproject.service.interfaces.account.CheckingServiceInterface;
import com.ironhack.midtermproject.service.interfaces.account.StudentCheckingServiceInterface;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;

@RestController
@RequestMapping("/api/checking")
@Slf4j
public class CheckingController implements CheckingControllerInterface {
    @Autowired
    private CheckingServiceInterface checkingService;
    @Autowired
    private StudentCheckingServiceInterface studentCheckingService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void saveChecking(@RequestBody (required=false) @Valid Checking checking) {
        LocalDate currentDate = LocalDate.now();
        AccountHolder accountHolder = (AccountHolder) checking.getPrimaryOwner();

        if (Period.between(currentDate, accountHolder.getDateOfBirth()).getYears() < 24) { // If the user is less than 24, create a student checking account
            log.info("The user is less than 24, a student checking account will be created");
            StudentChecking studentChecking;
            if (checking.getSecondaryOwner() == null) { // If there is not a secondary owner
                studentChecking = new StudentChecking(checking.getBalance(), checking.getPrimaryOwner(), checking.getSecretKeyChecking(), checking.getCreationDateChecking());
            } else {
                studentChecking = new StudentChecking(checking.getBalance(), checking.getPrimaryOwner(), checking.getSecondaryOwner(),checking.getSecretKeyChecking(), checking.getCreationDateChecking());
            }
            studentCheckingService.saveStudentChecking(studentChecking);
        }
        checkingService.saveChecking(checking);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Checking getChecking(@PathVariable Long id) {
        return checkingService.getChecking(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateChecking(@PathVariable Long id, @RequestBody(required=false) @Valid Checking checking) {
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
}
