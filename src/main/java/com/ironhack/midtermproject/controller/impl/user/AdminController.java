package com.ironhack.midtermproject.controller.impl.user;

import com.ironhack.midtermproject.controller.interfaces.user.AdminControllerInterface;
import com.ironhack.midtermproject.model.user.Admin;
import com.ironhack.midtermproject.service.interfaces.user.AdminServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController implements AdminControllerInterface {
    @Autowired
    private AdminServiceInterface adminService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAdmin(@RequestBody @Valid Admin admin) {
        adminService.saveAdmin(admin);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Admin getAdmin(@PathVariable Long id) {
        return adminService.getAdmin(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAdmin(@PathVariable Long id, @RequestBody @Valid Admin admin) {
        adminService.updateAdmin(id, admin);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
    }
}
