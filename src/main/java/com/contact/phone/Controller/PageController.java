package com.contact.phone.Controller;

import com.contact.phone.Entity.User;
import com.contact.phone.Forms.UserForm;
import com.contact.phone.Forms.UserFormEdit;
import com.contact.phone.Helper.Helper;
import com.contact.phone.Helper.Message;
import com.contact.phone.Helper.MessageType;
import com.contact.phone.Repository.UserRepository;
import com.contact.phone.services.ImageService;
import com.contact.phone.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    private static final Logger log = LoggerFactory.getLogger(PageController.class);

    @RequestMapping("/")
    public String index(){
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(){
        log.info("Home Controller running");
        return "home";
    }

    @RequestMapping("/about")
    public String about(){
        log.info("About Controller running");
        return "about";
    }

    @RequestMapping("/contact")
    public String contact(){
        log.info("Contact Controller running");
        return "contact";
    }

    @RequestMapping("/service")
    public String service(){
        log.info("Service Controller running");
        return "services";
    }

    @RequestMapping("/login")
    public String login(){
        log.info("Login Controller  running");
        return "login";
    }

    @RequestMapping("/signup")
    public String signup(Model model){
        UserForm userForm = new UserForm();
        log.info("Signup is running");
        model.addAttribute("userForm",userForm);
        return "signup";
    }

    @PostMapping("/do-register")
    public String dataRegister(
            @Valid @ModelAttribute("userForm") UserForm userForm,
            BindingResult bindingResult,
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                log.error("Field: {} | Error: {}", error.getField(), error.getDefaultMessage());
            });
            return "signup";
        }


        log.info("Run start");

        if (userService.isEmailExists(userForm.getEmail())) {
            session.setAttribute("message",
                    Message.builder()
                            .content("Email already registered")
                            .type(MessageType.red)
                            .build());
            return "signup";
        }

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setPassword(userForm.getPassword());
        user.setEnabled(false);

        log.info("profilePic ke pehla ");

        if (userForm.getProfileImage() != null && !userForm.getProfileImage().isEmpty()){
            String filename = UUID.randomUUID().toString();
            String uploaded = imageService.uploadImage(userForm.getProfileImage(), filename);
            user.setProfilePic(uploaded);
        }

        log.info("Data save ho nahi raha ");

        userService.saveUser(user);

        session.setAttribute("message",
                Message.builder()
                        .content("Registration successful! Please verify your email.")
                        .type(MessageType.success)
                        .build());

        return "redirect:/signup";
    }


    @GetMapping("/edit_profile_view")
    public String editProfileView(Authentication authentication,Model model){

        String loggedUser = Helper.getEmailofLoggedUser(authentication);

        User user = userService.getUserByEmail(loggedUser);

        UserFormEdit userForm  = new UserFormEdit();

        userForm.setName(user.getName());
        userForm.setAbout(user.getAbout());
        userForm.setImage(user.getProfilePic());
        userForm.setPhoneNumber(user.getPhoneNumber());

        model.addAttribute("userInfo",userForm);

        return "edit_profile";
    }

    @PostMapping("/edit_profile")
    public String editProfile(
            Authentication authentication,
            @Valid @ModelAttribute("userInfo") UserFormEdit userForm,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            log.info("Binding errors found");
            bindingResult.getFieldErrors().forEach(error -> {
                log.info("error : {} , field : {}",
                        error.getDefaultMessage(), error.getField());
            });
            return "edit_profile";
        }

        String userEmail = Helper.getEmailofLoggedUser(authentication);
        User user = userService.getUserByEmail(userEmail);

        // basic fields update
        user.setName(userForm.getName());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());

        // image update (only if new image selected)
        MultipartFile file = userForm.getProfileImage();
        if (file != null && !file.isEmpty()) {
            String filename = UUID.randomUUID().toString();
            String image = imageService.uploadImage(file, filename);
            user.setProfilePic(image);
        }

        userRepository.save(user);


        return "redirect:/user/dashboard";
    }



}
