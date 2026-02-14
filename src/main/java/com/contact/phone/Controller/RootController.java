package com.contact.phone.Controller;

import com.contact.phone.Entity.User;
import com.contact.phone.Helper.Helper;
import com.contact.phone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication){

        if (authentication == null){
            return;
        }

        String loggedUser = Helper.getEmailofLoggedUser(authentication);

        User userByEmail = userService.getUserByEmail(loggedUser);
        System.out.println(userByEmail.getEmail());
        System.out.println(userByEmail.getName());

        model.addAttribute("loggedUser",userByEmail);


    }

}
