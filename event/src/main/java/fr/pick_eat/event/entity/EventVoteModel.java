package fr.pick_eat.event.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class EventVoteModel {
    @Id
    @UuidGenerator
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "eventId", referencedColumnName = "id", nullable = false)
    private EventModel event;
    @ManyToOne
    @JoinColumn(name = "participantId", referencedColumnName = "id", nullable = false)
    private EventParticipantModel participant;
    private UUID restaurantId;
    private boolean isLiked;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EventModel getEvent() {
        return event;
    }

    public void setEvent(EventModel event) {
        this.event = event;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public EventParticipantModel getParticipant() {
        return participant;
    }

    public void setParticipant(EventParticipantModel participant) {
        this.participant = participant;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean like) {
        this.isLiked = like;
    }

    @Override
    public String toString() {
        return "EventVoteModel{" +
                "id=" + id +
                ", eventId=" + event.getId() +
                ", participant=" + participant.getId() +
                ", restaurantId=" + restaurantId +
                ", like=" + isLiked +
                '}';
    }
}
