package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.enums.Status;
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
}
