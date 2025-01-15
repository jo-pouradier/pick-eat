package fr.pick_eat.event.dto;

import java.util.UUID;

public class EventVoteDTO {
    private UUID eventId;
    private UUID restaurantId;
    private boolean isLiked;

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public boolean isLike() {
        return isLiked;
    }

    public void setLike(boolean like) {
        this.isLiked = like;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "EventVoteDTO{" +
                "restaurantId=" + restaurantId +
                ", like=" + isLiked +
                '}';
    }

}
