package com.contact.phone.Controller;

import com.contact.phone.Entity.Contact;
import com.contact.phone.Entity.User;
import com.contact.phone.Forms.ContactForm;
import com.contact.phone.Forms.ContactSearchForm;
import com.contact.phone.Helper.AppConstant;
import com.contact.phone.Helper.Helper;
import com.contact.phone.Helper.Message;
import com.contact.phone.Helper.MessageType;
import com.contact.phone.services.ContactService;
import com.contact.phone.services.ImageService;
import com.contact.phone.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;


    @RequestMapping("/add")
    public String contactView(Model model){
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm",contactForm);
        return "user/add_contact";
    }

    @PostMapping("/add")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result, Authentication authentication, HttpSession session){

        if (result.hasErrors()){
            session.setAttribute("message", Message.builder().content("Please fill form Correctly").type(MessageType.red).build());
            result.getAllErrors().forEach(System.out::println);
            return "user/add_contact";
        }

        String username = Helper.getEmailofLoggedUser(authentication);

        User user = this.userService.getUserByEmail(username);



        Contact contact = new Contact();

        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setFavourite(contactForm.isFavourite());
        contact.setFacebookLink(contactForm.getFacebookLink());
        contact.setLinkedinLink(contactForm.getLinkedInLink());

        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
            String filename = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(),filename);
            contact.setPicture(imageUrl);
            contact.setCloudinaryImagePublicId(filename);
        }

        System.out.println(contact);

        contactService.add(contact);

        session.setAttribute("message",Message.builder().content("Contact Add Successfully").type(MessageType.success).build());

        return "redirect:/user/contacts/add";
    }


    @RequestMapping
    public String view(
            @RequestParam(value = "page",defaultValue = AppConstant.Page) int page,
            @RequestParam(value = "size",defaultValue = AppConstant.Size) int size,
            @RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
            @RequestParam(value = "direction",defaultValue = "asc") String direction,
            Authentication authentication,Model model){

        String username = Helper.getEmailofLoggedUser(authentication);

        User user = userService.getUserByEmail(username);

        Page<Contact> contactsPage = contactService.getByUser(user,page,size,sortBy,direction);

        model.addAttribute("contactsPage",contactsPage);

        model.addAttribute("contactSearchForm",new ContactSearchForm());


        return "user/contacts";
    }

    @RequestMapping("/search")
    public String searchHandler(
            @ModelAttribute() ContactSearchForm contactSearchForm ,
            @RequestParam(value = "page",defaultValue = AppConstant.Page) int page,
            @RequestParam(value = "size",defaultValue = AppConstant.Size) int size,
            @RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
            @RequestParam(value = "order",defaultValue = "asc") String order,
            Model model,Authentication authentication
    ){

        Page<Contact> pageContacts = null;

        String loggedUser = Helper.getEmailofLoggedUser(authentication);

        User user = userService.getUserByEmail(loggedUser);

        if (contactSearchForm.getField().equalsIgnoreCase("name")){
            pageContacts = this.contactService.searchByNameKeyword(contactSearchForm.getValue(),page,size,sortBy,order,user);
        }

        else if (contactSearchForm.getField().equalsIgnoreCase("email")) {

            pageContacts = this.contactService.searchByEmailKeyword(contactSearchForm.getValue(),page,size,sortBy,order,user);
        }

        else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {

           pageContacts = this.contactService.searchByNumberKeyword(contactSearchForm.getValue(),page,size,sortBy,order,user);

        }

        model.addAttribute("contactsPage",pageContacts);


        model.addAttribute("contactSearchForm",contactSearchForm);

        return "user/search";
    }


    @RequestMapping("/delete/{contactId}")
    public String delete(@PathVariable String contactId, HttpSession session){
        this.contactService.delete(contactId);

        session.setAttribute("message", Message.builder().content("Contact Deleted Successfully").type(MessageType.success).build());

        return "redirect:/user/contacts";
    }

    @GetMapping("/view/{contactId}")
    public String updateViewForm(@PathVariable String contactId,Model model){

        Contact contact = contactService.getByID(contactId);

        ContactForm contactForm = new ContactForm();

        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFacebookLink(contact.getFacebookLink());
        contactForm.setLinkedInLink(contact.getLinkedinLink());
        contactForm.setFavourite(contact.isFavourite());
        contactForm.setImage(contact.getPicture());


        model.addAttribute("contactForm",contactForm);

        model.addAttribute("contactId",contactId);

        return "user/update_contact";
    }


    @PostMapping("/update/{contactId}")
    public String updateContact(@PathVariable String contactId ,@Valid  @ModelAttribute ContactForm contactForm,BindingResult result,Model model
                   ){


        if (result.hasErrors()){
            System.out.println("binding result works");
            return "user/update_contact";
        }

        var contact = contactService.getByID(contactId);
        contact.setContactId(contactId);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setFacebookLink(contactForm.getFacebookLink());
        contact.setLinkedinLink(contactForm.getLinkedInLink());
        contact.setFavourite(contactForm.isFavourite());

        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
            String filename = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), filename);
            contact.setCloudinaryImagePublicId(filename);
            contact.setPicture(imageUrl);
            contactForm.setImage(imageUrl);
        }

        contactService.update(contact);

        System.out.println(contact);
        model.addAttribute("message",Message.builder().content("Update Contact Successfully").type(MessageType.success).build());


         return "redirect:/user/contacts/view/{contactId}";
    }


}
