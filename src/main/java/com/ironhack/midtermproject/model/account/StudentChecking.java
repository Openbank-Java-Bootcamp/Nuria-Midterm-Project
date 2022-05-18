package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.utils.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "student_checking")
@PrimaryKeyJoinColumn(name = "id_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentChecking extends Account {
    @Enumerated(EnumType.STRING)
    private Status statusStudent;

    // Constructor with primary and secondary owners
    public StudentChecking(Money balance, User primaryOwner, User secondaryOwner, Long secretKey) {
        super(balance, primaryOwner, secondaryOwner, secretKey);
        this.statusStudent = Status.ACTIVE;
    }

    // Constructor with primary owner
    public StudentChecking(Money balance, User primaryOwner, Long secretKey) {
        super(balance, primaryOwner, secretKey);
        this.statusStudent = Status.ACTIVE;
    }
}
