package com.ironhack.midtermproject.model.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account_holders")
@PrimaryKeyJoinColumn(name = "id_user")
@Data
@NoArgsConstructor
public class Admin extends User {
}
