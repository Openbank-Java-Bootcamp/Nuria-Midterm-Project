package com.ironhack.midtermproject.model.user;

import com.ironhack.midtermproject.utils.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "account_holders")
@PrimaryKeyJoinColumn(name = "id_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountHolder extends User {
    @Column(name = "date_of_birth")
    @NotEmpty(message = "You must have a date of birth")
    private LocalDate dateOfBirth;

    @AttributeOverrides({
            @AttributeOverride(name = "streetAddress", column = @Column(name = "primary_street")),
            @AttributeOverride(name = "city", column = @Column(name = "primary_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "primary_postal"))
    })
    @Embedded
    @NotEmpty(message = "You must have a primary address")
    private Address primaryAddress;
    @AttributeOverrides({
            @AttributeOverride(name = "streetAddress", column = @Column(name = "secondary_street")),
            @AttributeOverride(name = "city", column = @Column(name = "secondary_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "secondary_postal"))
    })
    @Embedded
    private Address mailingAddress;

    // Account with primary address and mailing address
    public AccountHolder(String name, String username, String password, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress) {
        super(name, username, password);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    // Account with primary address
    public AccountHolder(String name, String username, String password, LocalDate dateOfBirth, Address primaryAddress) {
        super(name, username, password);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
    }
}
