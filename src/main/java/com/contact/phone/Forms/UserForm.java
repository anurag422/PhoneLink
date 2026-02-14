package com.contact.phone.Forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {

    @NotBlank(message = "Username is Required")
    @Size(min = 3, message = "min 3 character is required")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid Email Address")
    private String email;

    @NotBlank(message = "Password is Required")
    @Size(min = 6, message = "min 6 character is Required")
    private String password;

    @NotBlank(message = "About is Required")
    private String about;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Invalid phone number"
    )
    private String phoneNumber;

    private MultipartFile profileImage;

    private String image;
}
