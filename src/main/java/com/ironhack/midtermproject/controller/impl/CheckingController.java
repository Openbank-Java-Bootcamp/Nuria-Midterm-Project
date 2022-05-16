package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.controller.interfaces.CheckingControllerInterface;
import com.ironhack.midtermproject.model.account.Checking;
import com.ironhack.midtermproject.model.account.StudentChecking;
import com.ironhack.midtermproject.model.user.AccountHolder;
import com.ironhack.midtermproject.service.interfaces.CheckingServiceInterface;
import com.ironhack.midtermproject.service.interfaces.StudentCheckingServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;

@RestController
@RequestMapping("/api/checking")
public class CheckingController implements CheckingControllerInterface {
    @Autowired
    private CheckingServiceInterface checkingService;
    @Autowired
    private StudentCheckingServiceInterface studentCheckingService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void saveChecking(@RequestBody Checking checking) {
        LocalDate currentDate = LocalDate.now();
        AccountHolder accountHolder = (AccountHolder) checking.getPrimaryOwner();

        if (Period.between(currentDate, accountHolder.getDateOfBirth()).getYears() < 24) {
            StudentChecking studentChecking;
            if (checking.getPrimaryOwner() == null) {
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
    public void updateChecking(@PathVariable Long id, @RequestBody Checking checking) {
        checkingService.updateChecking(id, checking);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChecking(@PathVariable Long id) {
        checkingService.deleteChecking(id);
    }
}
