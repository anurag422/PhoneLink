package com.contact.phone.services;

import com.contact.phone.Entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;

public interface UserService {

     User saveUser(User user);

    Optional<User> updateUser(User user);

    Optional<User> getUserById(String userId);

    List<User> getAllUsers();

    void deleteUser(String userId);

    boolean isUserExist(String userId);

    boolean isUserByEmail(String email);

    User getUserByEmail(String email);

    boolean isEmailExists(String email);

}
