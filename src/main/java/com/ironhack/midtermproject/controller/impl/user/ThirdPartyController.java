package com.ironhack.midtermproject.controller.impl.user;

import com.ironhack.midtermproject.DTO.ThirdPartyDTO;
import com.ironhack.midtermproject.controller.interfaces.user.ThirdPartiControllerInterface;
import com.ironhack.midtermproject.model.user.ThirdParty;
import com.ironhack.midtermproject.service.interfaces.user.ThirdPartyServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/third")
public class ThirdPartyController implements ThirdPartiControllerInterface {
    @Autowired
    private ThirdPartyServiceInterface thirdPartyService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveThirdParty(@RequestBody ThirdPartyDTO thirdPartyDTO){
        thirdPartyService.saveThirdParty(thirdPartyDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ThirdParty getThirdParty(@PathVariable Long id) {
        return thirdPartyService.getThirdParty(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateThirdParty(@PathVariable Long id, @RequestBody(required=false) @Valid ThirdParty thirdParty) {
        thirdPartyService.updateThirdParty(id, thirdParty);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteThirdParty(@PathVariable Long id) {
        thirdPartyService.deleteThirdParty(id);
    }

    @PatchMapping("/transfer/{hashedKey}/{id}/{amount}/{secretKey}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(@PathVariable String hashedKey, @PathVariable BigDecimal amount, @PathVariable Long id, @PathVariable Long secretKey) {
        thirdPartyService.transferMoney(hashedKey, amount, id, secretKey);
    }

    @PatchMapping("/receive/{hashedKey}/{id}/{amount}/{secretKey}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void receiveMoney(@PathVariable String hashedKey, @PathVariable BigDecimal amount, @PathVariable Long id, @PathVariable Long secretKey) {
        thirdPartyService.receiveMoney(hashedKey, amount, id, secretKey);
    }
}
