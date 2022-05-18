package com.ironhack.midtermproject.service.impl.account;

import com.ironhack.midtermproject.DTO.CreditCardDTO;
import com.ironhack.midtermproject.model.account.CreditCard;
import com.ironhack.midtermproject.model.user.User;
import com.ironhack.midtermproject.repository.account.AccountRepository;
import com.ironhack.midtermproject.repository.account.CreditCardRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.service.interfaces.account.CreditCardServiceInterface;
import com.ironhack.midtermproject.utils.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
public class CreditCardService implements CreditCardServiceInterface {
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    public CreditCard saveCreditCard(CreditCardDTO creditCardDTO) {
        CreditCard creditCard = new CreditCard();
        if (creditCardDTO.getSecondaryOwner() == null) {
            Optional<User> user = userRepository.findById(creditCardDTO.getPrimaryOwner());
            creditCard = new CreditCard(creditCardDTO.getBalance(), user.get(), creditCardDTO.getSecretKey(), creditCardDTO.getCreditLimit(), creditCardDTO.getInterestRateCredit());
        } else {
            Optional<User> user1 = userRepository.findById(creditCardDTO.getPrimaryOwner());
            Optional<User> user2 = userRepository.findById(creditCardDTO.getSecondaryOwner());
            creditCard = new CreditCard(creditCardDTO.getBalance(), user1.get(), user2.get(), creditCardDTO.getSecretKey(), creditCardDTO.getCreditLimit(), creditCardDTO.getInterestRateCredit());
        }
        log.info("Saving a new credit card account {} inside of the database", creditCard.getAccountId());
        if (creditCard.getAccountId() != null) {
            Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(creditCard.getAccountId());
            if (optionalCreditCard.isPresent())
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account with id " + creditCard.getAccountId() + " already exist");
        }
        return creditCardRepository.save(creditCard);
    }

    public CreditCard getCreditCard(Long id) {
        log.info("Fetching credit card account {}", id);
        return creditCardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card account not found"));
    }

    public void updateCreditCard(Long id, CreditCard creditCard) {
        log.info("Updating credit card account {}", id);
        CreditCard creditCardFromDB = creditCardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card account not found"));
        creditCard.setAccountId(creditCardFromDB.getAccountId());
        creditCardRepository.save(creditCard);
    }

    public void deleteCreditCard(Long id) {
        log.info("Deleting credit card account {}", id);
        creditCardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card account not found"));
        if (creditCardRepository.findById(id).isPresent()) {
            creditCardRepository.deleteById(id);
        }
    }

    public void updateBalance(Long id, Money balance) {
        log.info("Updating balance of credit card account {}", id);
        CreditCard checkingFromDB = creditCardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card account is not found"));
        checkingFromDB.setBalance(balance);
        creditCardRepository.save(checkingFromDB);
    }

    public void transferMoney(String username, Long id, BigDecimal transfer) {
        log.info("Transferring money, {} will transfer", transfer);
        CreditCard thisCreditCard = (CreditCard) accountRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit Card account is not found"));
        CreditCard checkingReceiver = (CreditCard) accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit Card receiver account is not found"));

        if (thisCreditCard.getBalance().getAmount().compareTo(transfer) == -1) { // If the transfer is greater than the account balance
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer should be lower than " + thisCreditCard.getBalance().getAmount());
        } else {
            thisCreditCard.decreaseBalance(transfer); // Decrease the amount in the user account
            checkingReceiver.increaseBalance(transfer); // Increase the amount in the receiver account
        }
    }
}
