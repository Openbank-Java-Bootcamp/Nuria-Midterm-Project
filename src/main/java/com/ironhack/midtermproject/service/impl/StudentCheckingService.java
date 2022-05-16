package com.ironhack.midtermproject.service.impl;

import com.ironhack.midtermproject.model.account.StudentChecking;
import com.ironhack.midtermproject.repository.StudentCheckingRepository;
import com.ironhack.midtermproject.service.interfaces.StudentCheckingServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
public class StudentCheckingService implements StudentCheckingServiceInterface {
    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    public StudentChecking saveStudentChecking(StudentChecking studentChecking) {
        log.info("Saving a new student checking account {} inside of the database", studentChecking.getAccountId());
        if (studentChecking.getAccountId() != null) {
            Optional<StudentChecking> optionalStudentChecking = studentCheckingRepository.findById(studentChecking.getAccountId());
            if (optionalStudentChecking.isPresent())
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account with id " + studentChecking.getAccountId() + " already exist");
        }
        return studentCheckingRepository.save(studentChecking);
    }

    public StudentChecking getStudentChecking(Long id) {
        log.info("Fetching a student checking account");
        return studentCheckingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student checking account not found"));
    }

    public void updateStudentChecking(Long id, StudentChecking studentChecking) {
        StudentChecking studentCheckingFromDB = studentCheckingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student checking account not found"));
        studentChecking.setAccountId(studentCheckingFromDB.getAccountId());
        studentCheckingRepository.save(studentChecking);
    }

    public void deleteStudentChecking(Long id) {
        studentCheckingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student checking account not found"));
        if (studentCheckingRepository.findById(id).isPresent()) {
            studentCheckingRepository.deleteById(id);
        }
    }
}
