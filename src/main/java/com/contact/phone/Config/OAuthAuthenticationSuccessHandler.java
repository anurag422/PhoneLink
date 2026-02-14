package com.contact.phone.Config;

import com.contact.phone.Entity.Provider;
import com.contact.phone.Entity.User;
import com.contact.phone.Helper.AppConstant;
import com.contact.phone.Repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("OAuthAuthenticationSuccessHandler is running");

        var oAuth2AuthenticationToken= (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

        System.out.println(authorizedClientRegistrationId);

        var oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

         User user = new User();
         user.setUserId(UUID.randomUUID().toString());
         user.setRoles(List.of(AppConstant.Role_User));
         user.setEmailVerified(true);
         user.setEnabled(true);

         if (authorizedClientRegistrationId.equalsIgnoreCase("google")){

             user.setEmail(oAuth2User.getAttribute("email").toString());
             user.setProfilePic(oAuth2User.getAttribute("picture").toString());
             user.setProviderId(oAuth2User.getName());
             user.setName(oAuth2User.getAttribute("name").toString());
             user.setAbout("User is Created by Google");
             user.setProvider(Provider.GOOGLE);

         } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

             String email = oAuth2User.getAttribute("email").toString() != null ? oAuth2User.getAttribute("email").toString() : oAuth2User.getAttribute("login").toString() + "@gmail.com";
             String picture = oAuth2User.getAttribute("avatar_url").toString();
             String name = oAuth2User.getAttribute("login").toString();
             String providerUserId = oAuth2User.getName();

             user.setEmail(email);
             user.setName(name);
             user.setProviderId(providerUserId);
             user.setProfilePic(picture);
             user.setAbout("User is Created by Github");
             user.setProvider(Provider.GITHUB);
         }else {
             System.out.println("Unknown User");
         }

        User user1 = userRepository.findByEmail(user.getEmail()).orElse(null);
         if (user1 == null){
             userRepository.save(user);
         }

        new DefaultRedirectStrategy().sendRedirect(request,response,"/user/profile");

    }
}
