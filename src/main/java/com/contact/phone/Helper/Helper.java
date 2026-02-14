package com.contact.phone.Helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;

public class Helper {

    public static String getEmailofLoggedUser(Authentication authentication){


        if (authentication instanceof OAuth2AuthenticationToken){

            var oAuth2AuthenticationToken= (OAuth2AuthenticationToken)authentication;
            String clientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

           var oAuth2User =(OAuth2User) authentication.getPrincipal();

           String username="";

            if (clientRegistrationId.equalsIgnoreCase("google")) {

                System.out.println("Google email");
               username = oAuth2User.getAttribute("email").toString();

            } else if (clientRegistrationId.equalsIgnoreCase("github")) {

                System.out.println("github email");
                username = oAuth2User.getAttribute("email").toString() == null ? oAuth2User.getAttribute("login").toString()+"@gmail.com" : oAuth2User.getAttribute("email").toString();

            }
            return username;
        }else {
            return authentication.getName();
        }
    }


    public static String getEmailTokenForVerifyEmail(String emailToken){

        return "http://localhost:8080/auth/verify-email?token=" + emailToken;

    }

    public static String getEmailTokenForResetPassword(String emailToken){
        return "http://localhost:8080/auth/reset_password?token=" + emailToken;
    }


}
