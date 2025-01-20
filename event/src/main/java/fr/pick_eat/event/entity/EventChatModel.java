package fr.pick_eat.event.entity;

import fr.pick_eat.event.dto.EChatType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class EventChatModel {

    @Id
    @UuidGenerator
    private UUID chatId;
    private UUID eventId;
    private UUID userId;
    private String content;
    private String imagePath;
    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;
    private EChatType type;


    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}
