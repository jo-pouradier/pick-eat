package fr.pick_eat.auth.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GuestUserDto {

    private UUID uuid;

    private String firstName;

    private String lastName;

    public GuestUserDto() {
    }

    public GuestUserDto(UUID uuid, String firstName, String lastName) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public GuestUserDto setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public GuestUserDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public GuestUserDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
}