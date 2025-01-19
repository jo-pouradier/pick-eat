package fr.pick_eat.event.mapper;

import fr.pick_eat.event.dto.*;
import fr.pick_eat.event.entity.*;

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
        eventDTO.setSelectedRestaurantId(event.getSelectedRestaurantId());
        eventDTO.setOrganizerId(event.getOrganizerId());
        eventDTO.setDescription(event.getDescription());
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

    public static EventChatDto toEventChatDto(EventChatModel chat) {
        EventChatDto dto = new EventChatDto();
        dto.setChatId(chat.getChatId());
        dto.setEventId(chat.getEventId());
        dto.setUserId(chat.getUserId());
        dto.setContent(chat.getContent());
        dto.setImagePath(chat.getImagePath());
        dto.setDate(chat.getDate());
        dto.setType(chat.getType());
        return dto;
    }

    public static EventParticipantDTO toParticipantDTO(EventParticipantModel eventParticipantModel) {
        EventParticipantDTO dto = new EventParticipantDTO();
        dto.setUuid(eventParticipantModel.getId());
        dto.setEventId(eventParticipantModel.getEvent().getId());
        dto.setUserId(eventParticipantModel.getUserId());
        dto.setOrganizer(eventParticipantModel.isOrganizer());
        dto.setHasVoted(eventParticipantModel.isHasVoted());
        dto.setVotes(eventParticipantModel.getVotes().stream().map(EventMapper::toEventVoteDTO).toList());
        return dto;
    }
}
