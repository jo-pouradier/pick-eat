package fr.pick_eat.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBasicDto implements UserDetails {
    private UUID uuid = null;

    private String firstName = "";

    private String lastName = "";

    private String email = "";

    private Long githubId = null;

    private String roles = "";

    public UserBasicDto() {
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles.split(",")).map(role -> (GrantedAuthority) () -> role).toList();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return "**********";
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
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
    public UUID getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Long getGithubId() {
        return githubId;
    }

    public String getRoles() {
        return roles;
    }

    @JsonIgnore
    public boolean isGitHubUser() {
        return this.githubId != null;
    }

    public UserBasicDto setRoles(String roles) {
        this.roles = roles;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        UserBasicDto rhs = (UserBasicDto) obj;
        return this.uuid.equals(rhs.uuid);
    }
}