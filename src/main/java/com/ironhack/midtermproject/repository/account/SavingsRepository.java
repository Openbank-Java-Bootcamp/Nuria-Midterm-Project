package com.ironhack.midtermproject.repository.account;

import com.ironhack.midtermproject.model.account.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, Long> {
}
