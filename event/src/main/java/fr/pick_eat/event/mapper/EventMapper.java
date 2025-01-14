package fr.pick_eat.event.mapper;

import dto.EventDTO;
import dto.EventFeedbackDTO;
import dto.EventVoteDTO;
import fr.pick_eat.event.entity.EventFeedbackModel;
import fr.pick_eat.event.entity.EventModel;
import fr.pick_eat.event.entity.EventVoteModel;

public class EventMapper {


    public static EventDTO toEventDTO(EventModel event) {
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

    public static EventFeedbackDTO toEventFeedbackDTO(EventFeedbackModel entity) {
        EventFeedbackDTO feedback = new EventFeedbackDTO();
        feedback.setId(entity.getId());
        feedback.setEventId(entity.getEventId());
        feedback.setRating(entity.getRating());
        feedback.setComment(entity.getComment());
        feedback.setBucketName(entity.getBucketName());
        feedback.setPath(entity.getPath());
        return feedback;
    }

    public static EventVoteDTO toEventVoteDTO(EventVoteModel vote) {
        EventVoteDTO dto = new EventVoteDTO();
        dto.setRestaurantId(vote.getRestaurantId());
        dto.setLike(vote.isLiked());
        dto.setEventId(vote.getEvent().getId());
        return dto;
    }
}
