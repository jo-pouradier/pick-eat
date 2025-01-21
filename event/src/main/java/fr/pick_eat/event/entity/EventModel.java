package fr.pick_eat.event.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
public class EventModel {
    @Id
    @UuidGenerator
    private UUID id;
    private String name;
    private Date date;
    private String address;
    private Float latitude;
    private Float longitude;
    private Integer radius;
    @OneToMany(mappedBy = "event", fetch=FetchType.LAZY)
    private List<EventParticipantModel> participants;
    private String description;
    private UUID organizerId;
    private UUID selectedRestaurantId;
    @Getter
    @Setter
    @ElementCollection
    private List<String> types;
    @Getter
    @Setter
    private boolean isVoteFinished;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public List<EventParticipantModel> getParticipants() {
        return participants;
    }

    public void setOrganizerId(UUID organizerId) {
        this.organizerId = organizerId;
    }

    public UUID getOrganizerId() {
        return organizerId;
    }

    public void setSelectedRestaurantId(UUID selectedRestaurantId) {
        this.selectedRestaurantId = selectedRestaurantId;
    }

    public UUID getSelectedRestaurantId() {
        return selectedRestaurantId;
    }
}
