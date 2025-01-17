package fr.pick_eat.event.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class EventChatDto {

    private UUID chatId;
    private UUID eventId;
    private UUID userId;
    private String content;
    private String imagePath;
    private Date date;
    private EChatType type;
}

