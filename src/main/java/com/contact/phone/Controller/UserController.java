package com.contact.phone.Controller;

import com.contact.phone.Entity.User;
import com.contact.phone.Helper.Helper;
import com.contact.phone.services.ContactService;
import com.contact.phone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @RequestMapping("/dashboard")
    public String userDashboard(Authentication authentication,Model model){

        String loggedUserEmail = Helper.getEmailofLoggedUser(authentication);

        User user = userService.getUserByEmail(loggedUserEmail);

        Long countedContacts = contactService.countByUserContacts(user);
        Long countedFavourite = contactService.countFavouriteByUser(user);

        model.addAttribute("totalContacts",countedContacts);
        model.addAttribute("totalFavourite",countedFavourite);

        return "user/dashboard";
    }

    @RequestMapping("/profile")
    public String userProfile(){

        return "user/profile";
    }

}
