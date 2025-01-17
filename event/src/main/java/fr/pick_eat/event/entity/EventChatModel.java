package fr.pick_eat.event.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EventChatModel {

    @Id
    @UuidGenerator
    private UUID chatId;
    private UUID eventId;
    private UUID userId;
    private String content;
    private String imagePath;
    @CreationTimestamp
    private Date date;
}
