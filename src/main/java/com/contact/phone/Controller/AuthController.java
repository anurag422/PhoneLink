package com.contact.phone.Controller;

import com.contact.phone.Entity.ForgetPassword;
import com.contact.phone.Entity.User;
import com.contact.phone.Helper.Helper;
import com.contact.phone.Helper.Message;
import com.contact.phone.Helper.MessageType;
import com.contact.phone.Repository.ForgetPasswordRepository;
import com.contact.phone.Repository.UserRepository;
import com.contact.phone.services.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForgetPasswordRepository passwordRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, HttpSession session){

        Optional<User> optionalUser = userRepository.findByEmailToken(token);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            user.setEnabled(true);
            user.setEmailVerified(true);
            user.setEmailToken(null); // token clear after verification

            userRepository.save(user);

            session.setAttribute("message",
                    Message.builder()
                            .content("User Verified Successfully, Now you can Login")
                            .type(MessageType.success)
                            .build());

            return "success_page";
        }

        session.setAttribute("message",
                Message.builder()
                        .content("Invalid or Expired Token")
                        .type(MessageType.red)
                        .build());

        return "error_page";
    }

    @PostMapping("/forget_password")
    @Transactional
    public String forgetPassword(@RequestParam String email, HttpSession session){

        Optional<User> byEmail = userRepository.findByEmail(email);

        if (byEmail.isEmpty()){
            session.setAttribute("message",
                    Message.builder()
                            .content("Email Not Registered")
                            .type(MessageType.red)
                            .build());
            return "redirect:/auth/forget_password";
        }

        User user = byEmail.get();

        passwordRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();

        ForgetPassword forgetPassword = new ForgetPassword();
        forgetPassword.setToken(token);
        forgetPassword.setUser(user);
        forgetPassword.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        passwordRepository.save(forgetPassword);

        try {
            emailService.sendResetPasswordEmail(user.getEmail(), token);
        } catch (Exception e) {
            System.out.println("Reset email failed: " + e.getMessage());
        }

        session.setAttribute("message",
                Message.builder()
                        .content("Reset link sent to your email")
                        .type(MessageType.success)
                        .build());

        return "redirect:/login";
    }


    @GetMapping("/reset_password")
    public String resetPassword(@RequestParam String token, HttpSession session,Model model){

        ForgetPassword forgetPassword = passwordRepository.findByToken(token).orElse(null);

        if (forgetPassword == null || forgetPassword.getExpiryDate().isBefore(LocalDateTime.now())){
            session.setAttribute("message",Message.builder().content("Invalid or Expired token").type(MessageType.red).build());
            return "error_page";
        }

        model.addAttribute("token",token);
        return "reset_password";

    }


    @PostMapping("/reset_password")
    public String resetPassword(@RequestParam String token,@RequestParam String password,HttpSession session){

        ForgetPassword tokenVerify = passwordRepository.findByToken(token).orElse(null);

        if (tokenVerify == null || tokenVerify.getExpiryDate().isBefore(LocalDateTime.now())){
            session.setAttribute("message",Message.builder().content("Invalid or Expired Token").type(MessageType.red).build());
            return "redirect:/login";
        }

        User user = tokenVerify.getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        passwordRepository.delete(tokenVerify);

        session.setAttribute("message",Message.builder().content("Password Updated successfully").type(MessageType.success).build());
        return "redirect:/login";

    }

}
