package com.ironhack.midtermproject.repository.account;

import com.ironhack.midtermproject.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT user.name FROM Account INNER JOIN User ON account.primary_owner = user.id WHERE user.name = :name AND account.account_id = :id", nativeQuery = true)
    Optional<Account> findByNameAndAccountId(@Param("name") String name, @Param("id") Long id); // Find the account by the user's name and the account id
}
