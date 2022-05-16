package com.ironhack.midtermproject.model.account;

import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.utils.Money;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;

    private Money balance;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "primary_owner")
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "primary_user")),
            @AttributeOverride(name = "name", column = @Column(name = "primary_name")),
            @AttributeOverride(name = "username", column = @Column(name = "primary_username")),
            @AttributeOverride(name = "password", column = @Column(name = "primary_password"))
    })
    @Embedded
    private User primaryOwner;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "secondary_owner")
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "secondary_user")),
            @AttributeOverride(name = "name", column = @Column(name = "secondary_name")),
            @AttributeOverride(name = "username", column = @Column(name = "secondary_username")),
            @AttributeOverride(name = "password", column = @Column(name = "secondary_password"))
    })
    @Embedded
    private User secondaryOwner;

    @Column(name = "penalty_fee")
    private Money penaltyFee;
}
