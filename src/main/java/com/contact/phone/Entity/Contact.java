package com.contact.phone.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Contact {
    @Id
    private String contactId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String picture;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    private boolean favourite;
    private String facebookLink;
    private String linkedinLink;
    private String cloudinaryImagePublicId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "contactId")
    List<SocialLink> links = new ArrayList<>();
}
