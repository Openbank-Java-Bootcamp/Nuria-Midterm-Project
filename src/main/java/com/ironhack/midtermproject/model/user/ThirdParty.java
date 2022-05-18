package com.ironhack.midtermproject.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public ThirdParty(String name, String hashedKey) throws NoSuchAlgorithmException {
        this.name = name;

        // Bouncy castle library
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(
                hashedKey.getBytes(StandardCharsets.UTF_8));
        String sha256hex = new String(Hex.encode(hash));

        this.hashedKey = sha256hex;
    }
}
