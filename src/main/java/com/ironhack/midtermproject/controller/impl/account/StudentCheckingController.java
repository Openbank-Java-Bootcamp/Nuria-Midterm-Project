package com.ironhack.midtermproject.controller.impl.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.controller.interfaces.account.StudentCheckingControllerInterface;
import com.ironhack.midtermproject.model.account.StudentChecking;
import com.ironhack.midtermproject.service.interfaces.account.StudentCheckingServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentCheckingController implements StudentCheckingControllerInterface {
    @Autowired
    private StudentCheckingServiceInterface studentCheckingService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStudentChecking(@RequestBody(required=false) @Valid StudentChecking studentChecking) {
        studentCheckingService.saveStudentChecking(studentChecking);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentChecking getStudentChecking(@PathVariable Long id) {
        return studentCheckingService.getStudentChecking(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStudentChecking(@PathVariable Long id, @RequestBody(required=false) @Valid StudentChecking studentChecking) {
        studentCheckingService.updateStudentChecking(id, studentChecking);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentChecking(@PathVariable Long id) {
        studentCheckingService.deleteStudentChecking(id);
    }

    @PatchMapping("/{id}/balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable Long id, @RequestBody @Valid BalanceDTO balanceDTO) {
        studentCheckingService.updateBalance(id, balanceDTO.getBalance());
    }
}
