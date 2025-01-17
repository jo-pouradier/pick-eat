package fr.pick_eat.socketspring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SocketUserModel {
    @Id
    private UUID socketId;
    private UUID userId;
    private String username;
    private String role;
}
