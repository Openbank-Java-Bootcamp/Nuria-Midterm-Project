package com.ironhack.midtermproject.repository.account;

import com.ironhack.midtermproject.model.account.Account;
import com.ironhack.midtermproject.utils.Money;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT account_id FROM Account INNER JOIN User ON account.primary_owner = user.id WHERE user.username = :username", nativeQuery = true)
    Long findByUsername(@Param("username") String username); // Find the account by the username
    Optional<Account> findByAccountIdAndSecretKey(Long id, Long secretKey);
    @Query(value = "SELECT account.balance_amount FROM Account INNER JOIN User ON account.primary_owner = user.id WHERE account.account_id = :id AND user.username = :username", nativeQuery = true)
    Money findByIdAndUsername(@Param("id") Long id, @Param("username") String username);
}
