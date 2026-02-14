package com.contact.phone.Repository;

import com.contact.phone.Entity.ForgetPassword;
import com.contact.phone.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword,Long> {

    Optional<ForgetPassword> findByToken(String token);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM ForgetPassword t WHERE t.user = :user")
    void deleteByUser(@Param("user") User user);

}
