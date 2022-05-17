package com.ironhack.midtermproject.repository.account;

import com.ironhack.midtermproject.model.account.Checking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingRepository extends JpaRepository<Checking, Long> {
}
