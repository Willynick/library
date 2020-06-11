package com.online.library.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "usr")
public class User implements Serializable, UserDetails {
    @Id
    @JsonView(Views.IdName.class)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonView(Views.IdName.class)
    private String googleId;
    @JsonView(Views.IdName.class)
    @NotBlank(message = "Username cannot be empty")
    private String username;
    @JsonView(Views.FullMessage.class)
    private String userpic;
    @Email(message = "Email is not correct")
    @NotBlank(message = "Email cannot be empty")
    @JsonView(Views.FullMessage.class)
    private String email;

    private String activationCode;

    @JsonView(Views.FullMessage.class)
    private String gender;
    @JsonView(Views.FullMessage.class)
    private String locale;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.FullMessage.class)
    private LocalDateTime lastVisit;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @JsonView(Views.FullMessage.class)
    private Set<Role> roles;

    @JsonView(Views.FullMessage.class)
    private boolean hasSubscription = false;

    @JsonView(Views.FullMessage.class)
    @ElementCollection(targetClass=Long.class)
    private Set<Long> idBooks = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public LocalDateTime getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(LocalDateTime lastVisit) {
        this.lastVisit = lastVisit;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Set<Long> getIdBooks() {
        return idBooks;
    }

    public void setIdBooks(Set<Long> idBooks) {
        this.idBooks = idBooks;
    }

    public boolean isHasSubscription() {
        return hasSubscription;
    }

    public void setHasSubscription(boolean hasSubscription) {
        this.hasSubscription = hasSubscription;
    }

    @JsonView(Views.FullMessage.class)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return isActive();
    }

}
