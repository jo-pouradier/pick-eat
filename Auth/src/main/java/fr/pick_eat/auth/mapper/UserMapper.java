package fr.pick_eat.auth.mapper;

import fr.pick_eat.auth.dto.RegisterUserDto;
import fr.pick_eat.auth.dto.UserBasicDto;
import fr.pick_eat.auth.entity.UserBasic;

public class UserMapper {

    public static UserBasicDto toDto(UserBasic user) {
        return new UserBasicDto()
                .setUuid(user.getUuid())
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setGithubId(user.getGithubId())
                .setRoles(user.getRoles());
    }

    public static RegisterUserDto toRegisterUserDto(UserBasic user) {
        return new RegisterUserDto()
                .setPassword(user.getPassword())
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPassword(user.getPassword());
    }

}
