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
public class ThirdParty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "hashed_key")
    @NotEmpty(message = "You must have a hashed key")
    private String hashedKey;

    public ThirdParty(String name, String hashedKey) {
        this.name = name;
        this.hashedKey = String.valueOf(hashedKey.hashCode());
    }
}
