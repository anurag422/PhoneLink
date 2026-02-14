package com.contact.phone.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SocialLink {
    @Id
    private Long id;
    private String link;
    private String title;

    @ManyToOne
    @JoinColumn(name = "contactId")
    private Contact contactId;
}
