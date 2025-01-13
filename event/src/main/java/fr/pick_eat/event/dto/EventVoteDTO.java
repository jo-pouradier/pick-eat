package fr.pick_eat.event.dto;

import java.util.UUID;

import fr.pick_eat.event.entity.EventVoteModel;

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

    public static EventVoteDTO fromEntity(EventVoteModel vote) {
        EventVoteDTO dto = new EventVoteDTO();
        dto.setRestaurantId(vote.getRestaurantId());
        dto.setLike(vote.isLiked());
        dto.setEventId(vote.getEvent().getId());
        return dto;
    }

    @Override
    public String toString() {
        return "EventVoteDTO{" +
                "restaurantId=" + restaurantId +
                ", like=" + isLiked +
                '}';
    }

}
