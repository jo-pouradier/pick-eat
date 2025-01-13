package fr.pick_eat.auth.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Table(name = "users")
@Entity
@Getter
public class UserBasic implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(unique = true)
    private Long githubId;

    @Column()
    private String password;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "roles")
    private String roles = "";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles.split(",")).map(role -> (GrantedAuthority) () -> role).toList();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }

    public UserBasic setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBasic setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBasic setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBasic setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBasic setGithubId(Long githubId) {
        this.githubId = githubId;
        return this;
    }

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isGitHubUser() {
        return this.githubId != null;
    }

    public UserBasic setRoles(String roles) {
        this.roles = roles;
        return this;
    }

    public String getRoles() {
        return roles;
    }
}