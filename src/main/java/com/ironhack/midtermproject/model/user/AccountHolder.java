package com.ironhack.midtermproject.model.user;

import com.ironhack.midtermproject.utils.Address;
import jakarta.persistence.*;
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
    private LocalDate dateOfBirth;

    @AttributeOverrides({
            @AttributeOverride(name = "streetAddress", column = @Column(name = "primary_street")),
            @AttributeOverride(name = "city", column = @Column(name = "primary_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "primary_postal"))
    })
    @Embedded
    private Address primaryAddress;
    @AttributeOverrides({
            @AttributeOverride(name = "streetAddress", column = @Column(name = "secondary_street")),
            @AttributeOverride(name = "city", column = @Column(name = "secondary_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "secondary_postal"))
    })
    @Embedded
    private Address mailingAddress;
}
