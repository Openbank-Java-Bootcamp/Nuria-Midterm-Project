package com.ironhack.midtermproject.controller.impl.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.DTO.StudentCheckingDTO;
import com.ironhack.midtermproject.controller.interfaces.account.StudentCheckingControllerInterface;
import com.ironhack.midtermproject.model.account.StudentChecking;
import com.ironhack.midtermproject.service.interfaces.account.StudentCheckingServiceInterface;
import com.ironhack.midtermproject.utils.Money;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/student")
public class StudentCheckingController implements StudentCheckingControllerInterface {
    @Autowired
    private StudentCheckingServiceInterface studentCheckingService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStudentChecking(@RequestBody(required=false) @Valid StudentChecking studentChecking) {
        studentCheckingService.saveStudentChecking(studentChecking);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentChecking getStudentChecking(@PathVariable Long id) {
        return studentCheckingService.getStudentChecking(id);
    }

    @GetMapping("/{id}/balance/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Money getStudentBalance(@PathVariable Long id, @PathVariable String username) {
        return studentCheckingService.getStudentBalance(id, username);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStudentChecking(@PathVariable Long id, @RequestBody(required=false) @Valid StudentCheckingDTO studentCheckingDTO) {
        studentCheckingService.updateStudentChecking(id, studentCheckingDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentChecking(@PathVariable Long id) {
        studentCheckingService.deleteStudentChecking(id);
    }

    @PatchMapping("/{username}/{id}/balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable String username, @PathVariable Long id, @RequestBody @Valid BalanceDTO balanceDTO) {
        studentCheckingService.updateBalance(username, id, balanceDTO.getBalance());
    }

    @PatchMapping("/transfer/{username}/{id}/{transfer}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(@PathVariable String username, @PathVariable Long id, @PathVariable BigDecimal transfer) {
        studentCheckingService.transferMoney(username, id, transfer);
    }
}
