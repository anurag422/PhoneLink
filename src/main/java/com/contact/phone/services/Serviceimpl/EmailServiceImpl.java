package com.contact.phone.services.Serviceimpl;

import com.contact.phone.services.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${app.base.url}")
    private String baseUrl;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.brevo.com/v3/smtp/email")
            .build();

    public void sendEmail(String toEmail, String subject, String htmlContent) {

        Map<String, Object> body = Map.of(
                "sender", Map.of(
                        "name", "PhoneLink",
                        "email", "phonelink.official24@gmail.com"
                ),
                "to", new Object[]{
                        Map.of("email", toEmail)
                },
                "subject", subject,
                "htmlContent", htmlContent
        );

        webClient.post()
                .header("api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public void sendEmailWithHtml() {

    }

    @Override
    public void sendEmailWithAttachment() {

    }

    public void sendVerificationEmail(String toEmail, String userName, String token) {

        String verificationLink = baseUrl + "/auth/verify-email?token=" + token;

        String html = "<h3>Hello " + userName + "</h3>" +
                "<p>Please click below to verify your account:</p>" +
                "<a href='" + verificationLink + "'>Verify Account</a>";

        sendEmail(toEmail, "Verify Your PhoneLink Account", html);
    }

    public void sendResetPasswordEmail(String toEmail, String token) {

        String resetLink = baseUrl + "/auth/reset-password?token=" + token;

        String html = "<h3>Password Reset</h3>" +
                "<p>Click below to reset your password:</p>" +
                "<a href='" + resetLink + "'>Reset Password</a>";

        sendEmail(toEmail, "Reset Your PhoneLink Password", html);
    }


}