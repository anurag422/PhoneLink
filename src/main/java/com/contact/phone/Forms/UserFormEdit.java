package com.contact.phone.Forms;

import jakarta.persistence.Entity;
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
@AllArgsConstructor
@NoArgsConstructor
public class UserFormEdit {

    @NotBlank(message = "Username is Required")
    @Size(min = 3, message = "min 3 character is required")
    private String name;


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
