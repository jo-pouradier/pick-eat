package fr.pick_eat.auth.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Getter
public class UserBasicDto implements UserDetails {
    private UUID uuid;

    private String firstName;

    private String lastName;

    private String email;

    private Long githubId;

    private Date createdAt;

    private Date updatedAt;

    private String roles = "";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles.split(",")).map(role -> (GrantedAuthority) () -> role).toList();
    }

    @Override
    public String getPassword() {
        return "**********";
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

    public UserBasicDto setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public UserBasicDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBasicDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBasicDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBasicDto setGithubId(Long githubId) {
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

    public UserBasicDto setRoles(String roles) {
        this.roles = roles;
        return this;
    }


}