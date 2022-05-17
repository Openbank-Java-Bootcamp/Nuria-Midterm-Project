package com.ironhack.midtermproject.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Table(name = "third_party")
@PrimaryKeyJoinColumn(name = "id_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThirdParty extends User {
    @Column(name = "hashed_key")
    @NotEmpty(message = "You must have a hashed key")
    private String hashedKey;
}
