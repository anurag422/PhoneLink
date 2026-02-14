package com.contact.phone.services.Serviceimpl;

import com.contact.phone.Entity.User;
import com.contact.phone.Helper.AppConstant;
import com.contact.phone.Helper.Helper;
import com.contact.phone.Helper.ResourceNotFoundException;
import com.contact.phone.Repository.UserRepository;
import com.contact.phone.services.EmailService;
import com.contact.phone.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRoles(List.of(AppConstant.Role_User));


        String emailToken = UUID.randomUUID().toString();

        user.setEmailToken(emailToken);
        User saved = this.userRepository.save(user);
        String emailLink = Helper.getEmailTokenForVerifyEmail(emailToken);

        emailService.sendEmail(saved.getEmail(), "Verify Email Account : For PhoneLink","To verify the account press the Link "+emailLink);

        return saved;
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user1 = this.userRepository.findById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User is not Found"));

        user1.setName(user.getName());
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        user1.setAbout(user.getAbout());
        user1.setPhoneNumber(user.getPhoneNumber());
        user1.setProfilePic(user.getProfilePic());
        user1.setEnabled(user.isEnabled());
        user1.setEmailVerified(user.isEmailVerified());
        user1.setNumberVerified(user.isNumberVerified());
        user1.setProvider(user.getProvider());
        user1.setProviderId(user.getProviderId());

        User save = this.userRepository.save(user1);
        return Optional.ofNullable(save);
    }

    @Override
    public Optional<User> getUserById(String userId) {
        return this.userRepository.findById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public void deleteUser(String userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        this.userRepository.delete(user);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user!=null ? true : false;
    }

    @Override
    public boolean isUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user!=null ? true : false;
    }

    @Override
    public User getUserByEmail(String email) {
        return this
                .userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean isEmailExists(String email) {
        User user = this.userRepository.findByEmail(email).orElse(null);
        if (user == null){
            return false;
        }else {
            return true;
        }

    }
}
