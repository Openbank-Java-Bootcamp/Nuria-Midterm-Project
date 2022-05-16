package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.utils.Money;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "student_checking")
@PrimaryKeyJoinColumn(name = "id_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentChecking extends Account {
    @Column(name = "secret_key")
    private Long secretKeyStudent;

    @Column(name = "creation_date")
    private Date creationDateStudent;

    @Enumerated(EnumType.STRING)
    private Status statusStudent;

    public StudentChecking(Money balance, User primaryOwner, User secondaryOwner, Long secretKeyStudent, Date creationDateStudent) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKeyStudent = secretKeyStudent;
        this.creationDateStudent = creationDateStudent;
        this.statusStudent = Status.ACTIVE;
    }

    public StudentChecking(Money balance, User primaryOwner, Long secretKeyStudent, Date creationDateStudent) {
        super(balance, primaryOwner);
        this.secretKeyStudent = secretKeyStudent;
        this.creationDateStudent = creationDateStudent;
        this.statusStudent = Status.ACTIVE;
    }
}
