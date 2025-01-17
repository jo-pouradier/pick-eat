package fr.pick_eat.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
