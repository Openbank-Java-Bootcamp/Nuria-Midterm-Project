package com.ironhack.midtermproject.model.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "third_party")
@PrimaryKeyJoinColumn(name = "id_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThirdParty extends User {
    @Column(name = "hashed_key")
    private String hashedKey;
}
