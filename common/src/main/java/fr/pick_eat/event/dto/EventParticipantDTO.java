package fr.pick_eat.event.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class EventParticipantDTO {
    private UUID uuid;
    private UUID eventId;
    private UUID userId;
    private boolean isOrganizer;
    private boolean hasVoted;
    private List<EventVoteDTO> votes;
}
