package com.contact.phone.Config;

import com.contact.phone.services.Serviceimpl.SecurityCustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){

        httpSecurity.authorizeHttpRequests(auth->{
           auth.requestMatchers("/user/**").authenticated();
           auth.anyRequest().permitAll();
        });

        httpSecurity.formLogin(formlogin->{
            formlogin.loginPage("/login")
                    .loginProcessingUrl("/authenticated")
                    .defaultSuccessUrl("/user/dashboard",true)
                    .failureUrl("/login?error=true")
                    .usernameParameter("email")
                    .passwordParameter("password");

            formlogin.failureHandler(authFailureHandler);

        });




        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.logout(logoutform->{
           logoutform.logoutUrl("/do-logout");
           logoutform.logoutSuccessUrl("/login?logout=true");
        });

        //oAuth2 configuration

        httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });

        return httpSecurity.build();
    }

    @Bean
   public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, SecurityCustomUserDetailService securityCustomUserDetailService){
       DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(securityCustomUserDetailService);
       daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
       return daoAuthenticationProvider;
   }

   @Bean
   public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
   }

}
