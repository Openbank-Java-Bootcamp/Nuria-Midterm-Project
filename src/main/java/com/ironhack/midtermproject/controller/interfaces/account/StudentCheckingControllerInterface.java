package com.ironhack.midtermproject.controller.interfaces.account;

import com.ironhack.midtermproject.DTO.BalanceDTO;
import com.ironhack.midtermproject.DTO.StudentCheckingDTO;
import com.ironhack.midtermproject.model.account.StudentChecking;
import com.ironhack.midtermproject.utils.Money;

import java.math.BigDecimal;

public interface StudentCheckingControllerInterface {
    void saveStudentChecking(StudentChecking studentChecking);
    StudentChecking getStudentChecking(Long id);
    Money getStudentBalance(Long id, String username);
    void updateStudentChecking(Long id, StudentCheckingDTO studentCheckingDTO);
    void deleteStudentChecking(Long id);
    void updateBalance(String username, Long id, BalanceDTO balanceDTO);
    void transferMoney(String username, Long id, BigDecimal transfer);
}
