package com.contact.phone.Repository;

import com.contact.phone.Entity.Contact;
import com.contact.phone.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,String> {
    List<Contact> findByName(String name);

    Page<Contact> findByUser(User user, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    Page<Contact> findByUserAndNameContaining(User user,String name,Pageable pageable);

    Page<Contact> findByUserAndEmailContaining(User user,String email,Pageable pageable);

    Page<Contact> findByUserAndPhoneNumberContaining(User user,String phoneNumber,Pageable pageable);

    Long countByUser(User user);

    Long countByUserAndFavouriteTrue(User user);
}
