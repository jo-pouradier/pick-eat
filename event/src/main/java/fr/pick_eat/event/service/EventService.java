package fr.pick_eat.event.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import fr.pick_eat.event.mapper.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dto.EventDTO;
import dto.EventFeedbackDTO;
import dto.EventVoteDTO;
import fr.pick_eat.event.entity.EventFeedbackModel;
import fr.pick_eat.event.entity.EventModel;
import fr.pick_eat.event.entity.EventParticipantModel;
import fr.pick_eat.event.entity.EventVoteModel;
import fr.pick_eat.event.repository.EventFeedbackRepository;
import fr.pick_eat.event.repository.EventParticipantRepository;
import fr.pick_eat.event.repository.EventRepository;
import fr.pick_eat.event.repository.EventVoteRepository;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final EventFeedbackRepository eventFeedbackRepository;
    private final EventVoteRepository eventVoteRepository;

    public EventService(EventRepository eventRepository, EventParticipantRepository eventParticipantRepository,
            EventFeedbackRepository eventFeedbackRepository, EventVoteRepository eventVoteRepository) {
        this.eventRepository = eventRepository;
        this.eventParticipantRepository = eventParticipantRepository;
        this.eventFeedbackRepository = eventFeedbackRepository;
        this.eventVoteRepository = eventVoteRepository;
    }

    Logger log = LoggerFactory.getLogger(EventService.class);

    public List<EventModel> getHistory(UUID userUuid) {
        List<EventParticipantModel> participantEvents = eventParticipantRepository.findByUserId(userUuid);
        List<EventModel> events = participantEvents.stream().map(EventParticipantModel::getEvent).toList();
        log.info("User:{} history: {}", userUuid, events);
        return events;
    }

    public boolean joinEvent(UUID userUuid, UUID eventUuid) {
        EventModel event = eventRepository.findById(eventUuid).orElse(null);
        if (event == null) {
            log.error("Event {} does not exists", eventUuid);
            return false;
        }
        try {
            EventParticipantModel eventParticipant = new EventParticipantModel();
            eventParticipant.setEvent(event);
            eventParticipant.setUserId(userUuid);
            eventParticipantRepository.save(eventParticipant);
            log.info("User:{} joined Event:{}", userUuid, eventUuid);
            return true;
        } catch (Exception e) {
            log.error("Error while joining event", e.getMessage());
            return false;
        }
    }

    public EventFeedbackModel saveFeedbackEvent(EventFeedbackModel eventFeedback) {
        EventFeedbackModel savedFeedback = eventFeedbackRepository.save(eventFeedback);
        log.info("Feedback saved: " + savedFeedback);
        return savedFeedback;

    }

    public EventParticipantModel getEventParticipant(UUID userUuid, EventModel event) {
        return eventParticipantRepository.findByUserIdAndEventId(userUuid, event.getId());
    }

    public void leaveEvent(UUID userUuid, UUID eventId) {
        // eventParticipantRepository.deleteByUserIdAndEventId(userUuid, eventId);
        EventModel event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            log.error("Event {} does not exists", eventId);
            throw new NoSuchElementException("Event does not exists");
        }
        EventParticipantModel eventParticipant = event.getParticipants().stream()
                .filter(participant -> participant.getUserId().equals(userUuid)).findFirst().get();
        if (eventParticipant.isIsOrganizer()) {
            log.error("Organizer cannot leave event");
            throw new IllegalArgumentException("Organizer cannot leave event");
        }

        eventParticipantRepository.delete(eventParticipant);
    }

    public void saveSwipeEvent(UUID userUuid, UUID eventId) {
        EventModel event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            log.error("Event {} does not exists", eventId);
            throw new NoSuchElementException("Event does not exists");
        }
        EventParticipantModel eventParticipant = eventParticipantRepository.findByUserIdAndEventId(userUuid, event.getId());
        eventParticipant.setHasVoted(true);
        eventParticipantRepository.save(eventParticipant);
    }

    @Transactional
    public void saveVotes(UUID userId, List<EventVoteDTO> votes, UUID eventId) {
        EventModel event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            log.error("Event {} does not exists", eventId);
            throw new NoSuchElementException("Event does not exists");
        }
        EventParticipantModel eventParticipant = eventParticipantRepository.findByUserIdAndEventId(userId, event.getId());
        votes.forEach(vote -> {
            EventVoteModel eventVote = new EventVoteModel();
            eventVote.setParticipant(eventParticipant);
            eventVote.setEvent(event);
            eventVote.setRestaurantId(vote.getRestaurantId());
            eventVote.setLiked(vote.isLike());
            eventVoteRepository.save(eventVote);
        });
    }

    @Transactional
    public void registerUserVotes(UUID userUuid, UUID eventUuid, List<EventVoteDTO> votes) {
        EventParticipantModel participant;
        try{
        participant = eventParticipantRepository.findByUserIdAndEventId(userUuid, eventRepository
                .findById(eventUuid).get().getId());
        } catch (Exception e) {
            log.error("Error while getting participant", e.getMessage());
            throw new NoSuchElementException("Error while getting participant");
        }
        if (participant.isHasVoted()) {
            log.error("User has already voted");
            throw new Error("User has already voted");
        }
        participant.setHasVoted(true);
        EventModel event = participant.getEvent();
        try {
            eventParticipantRepository.save(participant);
        } catch (Exception e) {
            log.error("Error while saving participant", e.getMessage());
            throw new Error("Error while saving participant", e);
        }
        List<EventVoteModel> eventVotes;
        try {
            eventVotes = new ArrayList<>();
            votes.forEach(vote -> {
                EventVoteModel eventVote = new EventVoteModel();
                eventVote.setParticipant(participant);
                eventVote.setEvent(event);
                eventVote.setRestaurantId(vote.getRestaurantId());
                eventVote.setLiked(vote.isLike());
                eventVotes.add(eventVote);
            });
        } catch (Exception e) {
            log.error("Error while creating votes", e.getMessage());
            throw new Error("Error while creating votes", e);
        }
        try {
            eventVoteRepository.saveAll(eventVotes);
        } catch (Exception e) {
            log.error("Error while saving votes", e.getMessage());
            throw new Error("Error while saving votes", e);
        }
    }

    @Transactional
    public EventModel create(EventDTO eventDTO, UUID userUuid) {
        EventModel event = new EventModel();
        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setAddress(eventDTO.getAddress());
        event.setLatitude(eventDTO.getLatitude());
        event.setLongitude(eventDTO.getLongitude());
        event.setRadius(eventDTO.getRadius());

        EventModel savedEvent = eventRepository.save(event);

        EventParticipantModel eventParticipant = new EventParticipantModel();
        eventParticipant.setEvent(savedEvent);
        eventParticipant.setUserId(userUuid);
        eventParticipant.setIsOrganizer(true);
        eventParticipant.setHasVoted(false);
        eventParticipantRepository.save(eventParticipant);
        return savedEvent;
    }

    public EventFeedbackModel createEventFeedback(UUID userUuid, EventFeedbackDTO eventFeedbackDTO)
            throws NoSuchElementException, IllegalArgumentException {
        EventParticipantModel eventParticipant;
        try {
            EventModel event = eventRepository.findById(eventFeedbackDTO.getEventId()).orElse(null);
            if (event == null) {
                log.error("Event {} does not exists", eventFeedbackDTO.getEventId());
                throw new NoSuchElementException("Event does not exists");
            }
            try {
                eventParticipant = event.getParticipants().stream()
                        .filter(participant -> participant.getUserId().equals(userUuid)).findFirst().get();
            } catch (Exception e) {
                log.error("Error while getting participant, {}", e.getMessage());
                throw new NoSuchElementException("Error while getting participant");
            }
            log.info("User:{} is a participant of event:{}", userUuid, eventFeedbackDTO.getEventId());
        } catch (NoSuchElementException e) {
            log.error("User:{} is not a participant of event:{}", userUuid, eventFeedbackDTO.getEventId());
            throw new NoSuchElementException("User is not a participant of event");
        }

        EventFeedbackModel eventFeedback = new EventFeedbackModel();
        eventFeedback.setEventId(eventFeedbackDTO.getEventId());
        eventFeedback.setParticipantId(eventParticipant.getId());
        eventFeedback.setRating(eventFeedbackDTO.getRating());
        eventFeedback.setComment(eventFeedbackDTO.getComment());
        eventFeedback.setBucketName(eventFeedbackDTO.getBucketName());
        eventFeedback.setPath(eventFeedbackDTO.getPath());

        try {
            log.info(eventFeedback.toString());
            EventFeedbackModel feedback = eventFeedbackRepository.save(eventFeedback);
            log.info("Feedback saved: " + feedback);
            return feedback;
        } catch (IllegalArgumentException e) {
            log.error("Error while saving feedback", e.getMessage());
            throw new IllegalArgumentException("Error while saving feedback");
        }
    }

    public List<EventFeedbackModel> getFeedbackHistory(UUID userUuid) {
        List<EventParticipantModel> participantEvents = eventParticipantRepository.findByUserId(userUuid);
        if (participantEvents.isEmpty()) {
            log.error("User:{} has no feedbacks", userUuid);
            return new ArrayList<>();
        }

        List<EventFeedbackModel> feedbacks = new ArrayList<>();
        for (EventParticipantModel participant : participantEvents) {
            EventFeedbackModel feedbackList = eventFeedbackRepository.findByParticipantIdAndEventId(participant.getId(),
                    participant.getEvent().getId());
            if (feedbackList != null)
                feedbacks.add(feedbackList);
        }
        log.info("User:{} feedbacks: {}", userUuid, feedbacks);
        return feedbacks;
    }

    public List<EventVoteDTO> getSwipeHistory(UUID userUuid, UUID eventId) {
        EventModel event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            log.error("Event {} does not exists", eventId);
            throw new NoSuchElementException("Event does not exists");
        }
        EventParticipantModel eventParticipant = eventParticipantRepository.findByUserIdAndEventId(userUuid, event.getId());
        List<EventVoteModel> votes = eventParticipant.getVotes();
        List<EventVoteDTO> votesDTO = votes.stream().map(EventMapper::toEventVoteDTO).toList();
        log.info("User:{} votes: {}", userUuid, votesDTO);
        return votesDTO;
    }

    public List<String> getParticipants(UUID userUuid, UUID eventUuid) throws NoSuchElementException {
        EventModel event = eventRepository.findById(eventUuid).
                orElseThrow(() -> new NoSuchElementException("Event does not exists"));
        
        List<EventParticipantModel> participants = event.getParticipants();
        if (participants.isEmpty()) {
            log.error("Event:{} has no participants", eventUuid);
        return new ArrayList<>();
        }

        if (participants.stream().noneMatch(participant -> participant.getUserId().equals(userUuid))) {
            log.error("User:{} is not a participant of event:{}", userUuid, eventUuid);
            throw new NoSuchElementException("User is not a participant of event");
        }

        List<String> participantsList = participants.stream().map(participant -> participant.getUserId().toString()).toList();
        log.info("Event:{} participants: {}", eventUuid, participantsList);
        return participantsList;
    }

}
