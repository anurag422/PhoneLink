package com.contact.phone.services;

public interface EmailService {

    void sendEmail(String toEmail, String subject, String htmlContent);

    void sendEmailWithHtml();

    void sendEmailWithAttachment();

    void sendVerificationEmail(String toEmail, String userName, String token);


    void sendResetPasswordEmail(String toEmail, String token);

}
