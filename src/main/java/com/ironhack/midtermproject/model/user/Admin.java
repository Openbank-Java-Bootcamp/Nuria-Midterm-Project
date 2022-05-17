package com.ironhack.midtermproject.model.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "id_user")
@Data
@NoArgsConstructor
public class Admin extends User {
    public Admin(Long id, String name, String username, String password, Collection<Role> roles) {
        super(id, name, username, password, roles);
    }
}
