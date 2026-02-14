package com.contact.phone.Controller;

import com.contact.phone.Entity.Contact;
import com.contact.phone.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts/{contactsId}")
    public Contact getContact(@PathVariable("contactsId") String contactsId){
        return contactService.getByID(contactsId);
    }

}
