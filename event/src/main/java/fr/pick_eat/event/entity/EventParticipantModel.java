package fr.pick_eat.event.entity;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Table(indexes = {
        @Index(name = "idx_event_user", columnList = "eventId, userId")
}, 
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"eventId", "userId"})
})
@Entity
public class EventParticipantModel {
    @Id
    @UuidGenerator
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "eventId", referencedColumnName = "id", nullable = false)
    private EventModel event;
    private UUID userId;
    private boolean isOrganizer;
    private boolean hasVoted;
    @OneToMany(mappedBy = "participant")
    private List<EventVoteModel> votes;

    public EventModel getEvent() {
        return event;
    }

    public void setEvent(EventModel event) {
        this.event = event;
    }

    public boolean isOrganizer() {
        return isOrganizer;
    }

    public void setOrganizer(boolean isOrganizer) {
        this.isOrganizer = isOrganizer;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public boolean isIsOrganizer() {
        return isOrganizer;
    }

    public void setIsOrganizer(boolean isOrganizer) {
        this.isOrganizer = isOrganizer;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public List<EventVoteModel> getVotes() {
        return votes;
    }

    public void setVotes(List<EventVoteModel> votes) {
        this.votes = votes;
    }

}
