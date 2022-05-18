package com.ironhack.midtermproject.repository.account;

import com.ironhack.midtermproject.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT user.username FROM Account INNER JOIN User ON account.primary_owner = user.id WHERE user.username = :username", nativeQuery = true)
    Optional<Account> findByUsername(@Param("username") String username); // Find the account by the username
    Optional<Account> findByAccountIdAndSecretKey(Long id, Long secretKey);
}
