package fr.pick_eat.event.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
public class EventDTO {
    private UUID id;
    private String name;
    private Date date;
    private String address;
    private Float latitude;
    private Float longitude;
    private Integer radius;
    private String description;
    private UUID organizerId;
    private UUID selectedRestaurantId;
    private boolean isVoteFinished;

}
