package com.contact.phone.Forms;

import com.contact.phone.Helper.Message;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactForm {

    @NotBlank(message = "Username is Required")
    @Size(min = 3,message = "min 3 character Required")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Phone Number is Required")
    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Invalid phone number"
    )
    private String phoneNumber;

    @NotBlank(message = "Address is Required")
    private String address;

    @NotBlank(message = "Description is Required")
    private String description;

    private String facebookLink;
    private String linkedInLink;
    private boolean favourite;
    private MultipartFile contactImage;

    private String image;

}
