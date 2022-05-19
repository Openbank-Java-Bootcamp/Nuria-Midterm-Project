package com.ironhack.midtermproject.model.user;

import com.google.common.hash.Hashing;
import jakarta.persistence.*;
import lombok.*;

import java.nio.charset.StandardCharsets;

@Entity
@Table(name = "third_party")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThirdParty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "hashed_key")
    private String hashedKey;

    public ThirdParty(String name, String hashedKey) {
        this.name = name;
        this.hashedKey = Hashing.sha256().hashString(hashedKey, StandardCharsets.UTF_8).toString();
    }
}
