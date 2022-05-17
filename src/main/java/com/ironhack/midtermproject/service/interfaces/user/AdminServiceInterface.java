package com.ironhack.midtermproject.service.interfaces.user;

import com.ironhack.midtermproject.model.user.Admin;

public interface AdminServiceInterface {
    Admin saveAdmin(Admin admin);
    Admin getAdmin(Long id);
    void updateAdmin(Long id, Admin admin);
    void deleteAdmin(Long id);
}
