package com.contact.phone.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User implements UserDetails {
    @Id
    private String userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false,unique = true)
    private String email;
    @Getter(AccessLevel.NONE)
    private String password;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String about;
    @Column(length = 500)
    private String profilePic;
    private String phoneNumber;

    @Getter(AccessLevel.NONE)
    private boolean enabled=true;

    private boolean emailVerified=false;
    private boolean numberVerified=false;

    private String emailToken;

    @Enumerated(value = EnumType.STRING)
    private Provider provider = Provider.SELF;
    private String providerId;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user",orphanRemoval = true)
    List<Contact> contacts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles=new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> collect = roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
