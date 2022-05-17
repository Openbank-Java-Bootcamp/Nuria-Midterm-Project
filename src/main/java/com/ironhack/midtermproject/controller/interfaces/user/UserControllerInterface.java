package com.ironhack.midtermproject.controller.interfaces.user;

import com.ironhack.midtermproject.model.user.User;

import java.util.List;

public interface UserControllerInterface {
    List<User> getUsers();
    void saveUser(User user);
}
