package com.ironhack.midtermproject.controller.interfaces;

import com.ironhack.midtermproject.model.user.User;

import java.util.List;

public interface UserControllerInterface {
    List<User> getUsers();
    void saveUser(User user);
}
