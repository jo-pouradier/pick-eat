package fr.pick_eat.event.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

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
    private List<String> types;
    private boolean isVoteFinished;
}
