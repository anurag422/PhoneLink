package com.contact.phone.services.Serviceimpl;

import com.contact.phone.Entity.Contact;
import com.contact.phone.Entity.User;
import com.contact.phone.Helper.ResourceNotFoundException;
import com.contact.phone.Repository.ContactRepository;
import com.contact.phone.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact add(Contact contact) {
        contact.setContactId(UUID.randomUUID().toString());
        return contactRepository.save(contact);
    }

    @Override
    public Contact update(Contact contact) {

        Contact found = this.contactRepository.findById(contact.getContactId()).orElseThrow(() -> new ResourceNotFoundException("Contact is not found"));

        found.setName(contact.getName());
        found.setEmail(contact.getEmail());
        found.setAddress(contact.getAddress());
        found.setDescription(contact.getDescription());
        found.setPhoneNumber(contact.getPhoneNumber());
        found.setFavourite(contact.isFavourite());
        found.setPicture(contact.getPicture());
        found.setLinkedinLink(contact.getLinkedinLink());
        found.setFacebookLink(contact.getFacebookLink());
        found.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());


        return this.contactRepository.save(found);
    }

    @Override
    public List<Contact> getContact() {
        return contactRepository.findAll();
    }

    @Override
    public Contact getByID(String id) {
        return contactRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact is not Found"));
    }

    @Override
    public void delete(String id) {
        Contact found = contactRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact is not Found"));
        contactRepository.delete(found);
    }

    @Override
    public Long countByUserContacts(User user) {
        return contactRepository.countByUser(user);
    }

    @Override
    public Long countFavouriteByUser(User user) {
        return contactRepository.countByUserAndFavouriteTrue(user);
    }

    @Override
    public Page<Contact> searchByNameKeyword(String name, int page, int size, String sortBy, String order,User user) {

        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pagebale = PageRequest.of(page,size,sort);

        return this.contactRepository.findByUserAndNameContaining(user,name, pagebale);
    }

    @Override
    public Page<Contact> searchByEmailKeyword(String email, int page, int size, String sortBy, String order,User user) {

        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pagebale = PageRequest.of(page,size,sort);

        return this.contactRepository.findByUserAndEmailContaining(user,email, pagebale);

    }

    @Override
    public Page<Contact> searchByNumberKeyword(String phoneNumber, int page, int size, String sortBy, String order,User user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pagebale = PageRequest.of(page,size,sort);

        return this.contactRepository.findByUserAndPhoneNumberContaining(user,phoneNumber, pagebale);
    }


    @Override
    public List<Contact> getByUserId(String userID) {
        return contactRepository.findByUserId(userID);
    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size,String sortBy,String order) {

        Sort sort =order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

       var pagebale = PageRequest.of(page,size,sort);

        return contactRepository.findByUser(user,pagebale);
    }
}
