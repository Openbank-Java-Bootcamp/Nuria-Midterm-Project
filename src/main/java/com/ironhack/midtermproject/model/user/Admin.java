package com.ironhack.midtermproject.model.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account_holders")
@PrimaryKeyJoinColumn(name = "id_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends User {
}
