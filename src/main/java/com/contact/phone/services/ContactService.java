package com.contact.phone.services;

import com.contact.phone.Entity.Contact;
import com.contact.phone.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ContactService {

    Contact add(Contact contact);

    Contact update(Contact contact);

    List<Contact> getContact();

    Contact getByID(String id);

    void delete(String id);

    Long countByUserContacts(User user);

    Long countFavouriteByUser(User user);

    Page<Contact> searchByNameKeyword(String name,int page,int size,String sortBy,String order,User user);

    Page<Contact> searchByEmailKeyword(String email,int page,int size,String sortBy,String order,User user);

    Page<Contact> searchByNumberKeyword(String phoneNumber,int page,int size,String sortBy,String order,User user);

    List<Contact> getByUserId(String userID);

    Page<Contact> getByUser(User user, int page, int size,String sortBy,String order);


}
