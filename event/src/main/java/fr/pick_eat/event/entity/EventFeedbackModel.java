package fr.pick_eat.event.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class EventFeedbackModel {
    @Id
    @UuidGenerator
    private UUID id;
    private UUID eventId;
    private UUID participantId;
    private double rating;
    private String comment;
    private String bucketName;
    private String path;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getParticipantId() {
        return participantId;
    }

    public void setParticipantId(UUID userId) {
        this.participantId = userId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "EventFeedbackModel{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", participantId=" + participantId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

}
