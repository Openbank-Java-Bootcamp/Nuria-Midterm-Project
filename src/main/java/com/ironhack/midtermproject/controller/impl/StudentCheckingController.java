package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.controller.interfaces.StudentCheckingControllerInterface;
import com.ironhack.midtermproject.model.account.StudentChecking;
import com.ironhack.midtermproject.service.interfaces.StudentCheckingServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student_checking")
public class StudentCheckingController implements StudentCheckingControllerInterface {
    @Autowired
    private StudentCheckingServiceInterface studentCheckingService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStudentChecking(@RequestBody StudentChecking studentChecking) {
        studentCheckingService.saveStudentChecking(studentChecking);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentChecking getStudentChecking(@PathVariable Long id) {
        return studentCheckingService.getStudentChecking(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStudentChecking(@PathVariable Long id, @RequestBody StudentChecking studentChecking) {
        studentCheckingService.updateStudentChecking(id, studentChecking);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentChecking(@PathVariable Long id) {
        studentCheckingService.deleteStudentChecking(id);
    }
}
