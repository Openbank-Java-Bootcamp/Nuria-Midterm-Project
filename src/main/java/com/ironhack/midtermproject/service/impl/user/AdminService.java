package com.ironhack.midtermproject.service.impl.user;

import com.ironhack.midtermproject.model.user.Admin;
import com.ironhack.midtermproject.repository.user.AdminRepository;
import com.ironhack.midtermproject.service.interfaces.user.AdminServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
public class AdminService implements AdminServiceInterface {
    @Autowired
    private AdminRepository adminRepository;

    public Admin saveAdmin(Admin admin) {
        log.info("Saving a new admin account {} inside of the database", admin.getId());
        if (admin.getId() != null) {
            Optional<Admin> optionalAdmin = adminRepository.findById(admin.getId());
            if (optionalAdmin.isPresent())
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User with id " + admin.getId() + " already exist");
        }
        return adminRepository.save(admin);
    }

    public Admin getAdmin(Long id) {
        log.info("Fetching admin account {}", id);
        return adminRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin user not found"));
    }

    public void updateAdmin(Long id, Admin admin) {
        log.info("Updating admin user {}", id);
        Admin adminFromDB = adminRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin user not found"));
        admin.setId(adminFromDB.getId());
        adminRepository.save(admin);
    }

    public void deleteAdmin(Long id) {
        log.info("Deleting admin user {}", id);
        adminRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin user not found"));
        if (adminRepository.findById(id).isPresent()) {
            adminRepository.deleteById(id);
        }
    }
}
