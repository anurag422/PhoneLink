package com.contact.phone;

import com.contact.phone.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PhoneLinkApplicationTests {

    @Autowired
    private EmailService service;

	@Test
	void contextLoads() {
	}

    @Test
    void sendEmailTest(){
        service.sendEmail(
                "prajapatanurag972@gmail.com",
                "Test the Email",
                "Testing the email is working properly or not on my PhoneLink project "
        );
    }

}
