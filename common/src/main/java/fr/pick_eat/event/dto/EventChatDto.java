package fr.pick_eat.event.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class EventChatDto {

    private UUID chatId;
    private UUID eventId;
    private UUID userId;
    private String content;
    private String imagePath;
    private Date date;
}
