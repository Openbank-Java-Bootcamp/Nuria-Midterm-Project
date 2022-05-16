package com.ironhack.midtermproject.controller.interfaces;

import com.ironhack.midtermproject.model.account.StudentChecking;

public interface StudentCheckingControllerInterface {
    void saveStudentChecking(StudentChecking studentChecking);
    StudentChecking getStudentChecking(Long id);
    void updateStudentChecking(Long id, StudentChecking studentChecking);
    void deleteStudentChecking(Long id);
}
