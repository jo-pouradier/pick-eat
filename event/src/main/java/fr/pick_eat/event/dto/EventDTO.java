package fr.pick_eat.event.dto;

import java.util.Date;
import java.util.UUID;

import fr.pick_eat.event.entity.EventModel;

public class EventDTO {
    private UUID id;
    private String name;
    private Date date;
    private String address;
    private Float latitude;
    private Float longitude;
    private Integer radius;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public static EventDTO fromEntity(EventModel event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setName(event.getName());
        eventDTO.setDate(event.getDate());
        eventDTO.setAddress(event.getAddress());
        eventDTO.setLatitude(event.getLatitude());
        eventDTO.setLongitude(event.getLongitude());
        eventDTO.setRadius(event.getRadius());
        return eventDTO;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
