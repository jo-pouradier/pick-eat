package fr.pick_eat.event.dto;

import java.util.UUID;

public class EventFeedbackDTO {
    private UUID id;
    private UUID eventId;
    private double rating;
    private String comment;
    private String bucketName;
    private String path;

    public EventFeedbackDTO() {
    }

    public EventFeedbackDTO(UUID eventId, double rating, String comment, String bucketName, String path) {
        this.eventId = eventId;
        this.rating = rating;
        this.comment = comment;
        this.bucketName = bucketName;
        this.path = path;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
