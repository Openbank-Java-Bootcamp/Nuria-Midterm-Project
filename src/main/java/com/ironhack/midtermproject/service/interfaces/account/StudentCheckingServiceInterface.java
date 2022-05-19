package com.ironhack.midtermproject.service.interfaces.account;

import com.ironhack.midtermproject.DTO.StudentCheckingDTO;
import com.ironhack.midtermproject.model.account.StudentChecking;
import com.ironhack.midtermproject.utils.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface StudentCheckingServiceInterface {
    StudentChecking saveStudentChecking(StudentChecking studentChecking);
    StudentChecking getStudentChecking(Long id);
    Money getStudentBalance(Long id, String username);
    void updateStudentChecking(Long id, StudentCheckingDTO studentCheckingDTO);
    void deleteStudentChecking(Long id);
    void updateBalance(String username, Long id, Money balance);
    void transferMoney(String name, Long id, BigDecimal transfer);
}
