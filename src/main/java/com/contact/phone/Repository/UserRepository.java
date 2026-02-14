package com.contact.phone.Repository;

import com.contact.phone.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailToken(String token);

}
