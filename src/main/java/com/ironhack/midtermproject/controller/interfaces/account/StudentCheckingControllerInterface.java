package com.ironhack.midtermproject.controller.interfaces.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.model.account.StudentChecking;

public interface StudentCheckingControllerInterface {
    void saveStudentChecking(StudentChecking studentChecking);
    StudentChecking getStudentChecking(Long id);
    void updateStudentChecking(Long id, StudentChecking studentChecking);
    void deleteStudentChecking(Long id);
    void updateBalance(Long id, BalanceDTO balanceDTO);
}
