package com.ironhack.midtermproject.service.interfaces;

import com.ironhack.midtermproject.model.user.User;

import java.util.List;

public interface UserServiceInterface {
    User saveUser(User user);
    List<User> getUsers();
}
