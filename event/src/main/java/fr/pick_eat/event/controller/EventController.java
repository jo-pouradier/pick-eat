package fr.pick_eat.event.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import fr.pick_eat.event.mapper.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.pick_eat.event.dto.EventDTO;
import fr.pick_eat.event.dto.EventFeedbackDTO;
import fr.pick_eat.event.dto.EventVoteDTO;
import fr.pick_eat.event.entity.EventFeedbackModel;
import fr.pick_eat.event.entity.EventModel;
import fr.pick_eat.event.service.EventService;
import fr.pick_eat.event.utils.JWTUtils;
import io.swagger.v3.oas.annotations.Parameter;

@CrossOrigin
@RestController
public class EventController {
    private final EventService eventService;
    private final Logger log = LoggerFactory.getLogger(EventController.class);
    private final JwtDecoder decoder;

    public EventController(EventService eventService, JwtDecoder jwtDecoder) {
        this.eventService = eventService;
        this.decoder = jwtDecoder;
    }

    @GetMapping("/{eventUuid}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable("eventUuid") UUID eventUuid) {
        EventModel event = eventService.getEvent(eventUuid);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        EventDTO eventDTO = EventMapper.toEventDTO(event);
        return ResponseEntity.ok(eventDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createEvent(@RequestBody EventDTO eventDTO,
    @Parameter(hidden=true) @CookieValue("jwt") String jwt) {
        
        String userId = JWTUtils.extractUserIdCookie(jwt, decoder);
        UUID userUuid = UUID.fromString(userId);
        log.info("Creating event for user {}", userUuid);

        EventModel event;
        try {
            event = eventService.create(eventDTO, userUuid);
        } catch (Exception e) {
            log.error("Error while creating event", e);
            return ResponseEntity.badRequest().body("Error while creating event");
        }
        return ResponseEntity.ok(event.getId().toString());
    }

    @GetMapping("/history")
    public ResponseEntity<List<EventDTO>> getHistory(@Parameter(hidden=true) @CookieValue("jwt") String jwt) {
        String userId = JWTUtils.extractUserIdCookie(jwt, decoder);
        UUID userUuid = UUID.fromString(userId);

        List<EventModel> events = eventService.getHistory(userUuid);
        List<EventDTO> eventsDTO = events.stream().map(EventMapper::toEventDTO).toList();
        return ResponseEntity.ok(eventsDTO);
    }

    @PostMapping("/join/{eventId}")
    public ResponseEntity<String> joinEvent(@Parameter(hidden=true) @CookieValue("jwt") String jwt,
            @PathVariable("eventId") UUID eventUuid) {
        String userId = JWTUtils.extractUserIdCookie(jwt, decoder);
        UUID userUuid = UUID.fromString(userId);

        Boolean isJoined = false;

        try{ 
            isJoined = eventService.joinEvent(userUuid, eventUuid);
        } catch (IllegalArgumentException e) {
            log.error("Error while joining event", e);
            return ResponseEntity.badRequest().body("Error while joining event");
        } catch (NoSuchElementException e) {
            log.error("Error while joining event", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error while joining event", e);
            return ResponseEntity.badRequest().body("Error while joining event");
        }

        return ResponseEntity.ok(isJoined.toString());
    }

    @PostMapping("/feedback/{eventId}")
    public ResponseEntity<EventFeedbackDTO> submitFeedback(@Parameter(hidden=true) @CookieValue("jwt") String jwt,
            @RequestBody EventFeedbackDTO eventFeedbackDTO, @PathVariable("eventId") UUID eventId) {
        String userId = JWTUtils.extractUserIdCookie(jwt, decoder);
        UUID userUuid = UUID.fromString(userId);
        if (!eventFeedbackDTO.getEventId().equals(eventId)) {
            return ResponseEntity.status(412).body(null); // precondition failed
        }

        EventFeedbackModel savedEventFeedback;
        try {
            savedEventFeedback = eventService.createEventFeedback(userUuid, eventFeedbackDTO);
        } catch (NoSuchElementException e) {
            log.error("Error while saving feedback", e);
            return ResponseEntity.notFound().build(); // 404
        } catch (IllegalArgumentException e) {
            log.error("Error while saving feedback", e);
            return ResponseEntity.status(406).build(); // no acceptable
        } catch (Exception e) {
            log.error("Error while saving feedback", e);
            return ResponseEntity.badRequest().build(); // 400
        }

        EventFeedbackDTO feedbackDTO = EventMapper.toEventFeedbackDTO(savedEventFeedback);
        return ResponseEntity.ok(feedbackDTO);
    }

    @GetMapping("/feedback/history")
    public ResponseEntity<List<EventFeedbackDTO>> getFeedbackHistory(@Parameter(hidden=true) @CookieValue("jwt") String jwt) {
        String userId = JWTUtils.extractUserIdCookie(jwt, decoder);
        UUID userUuid = UUID.fromString(userId);

        List<EventFeedbackModel> feedbacks = eventService.getFeedbackHistory(userUuid);
        List<EventFeedbackDTO> feedbacksDTO = feedbacks.stream().map(EventMapper::toEventFeedbackDTO).toList();
        return ResponseEntity.ok(feedbacksDTO);
    }

    @PostMapping("/leave/{eventId}")
    public ResponseEntity<Boolean> leaveEvent(@Parameter(hidden=true) @CookieValue("jwt") String jwt,
            @PathVariable("eventId") UUID eventId) {
        String userId = JWTUtils.extractUserIdCookie(jwt, decoder);
        UUID userUuid = UUID.fromString(userId);

        try {
            eventService.leaveEvent(userUuid, eventId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.ok(true);
    }

    @PostMapping("/swipe/{eventId}")
    public ResponseEntity<Boolean> swipeEvent(@Parameter(hidden=true) @CookieValue("jwt") String jwt,
            @PathVariable("eventId") UUID eventId,
            @RequestBody List<EventVoteDTO> votes) {
        String userId = JWTUtils.extractUserIdCookie(jwt, decoder);
        UUID userUuid = UUID.fromString(userId);

        try {
            eventService.registerUserVotes(userUuid, eventId, votes);
        } catch (NoSuchElementException e) {
            log.error("Error while saving votes", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Error e) {
            if (e.getMessage().equals("User has already voted")) {
                log.error("User has already voted", e.getMessage());
                return ResponseEntity.status(409).body(false); // conflict
            }
        } catch (Exception e) {
            log.error("Error while saving votes", e);
            return ResponseEntity.badRequest().body(false);
        }

        return ResponseEntity.ok(true);
    }

    @GetMapping("/swipe/history/{eventId}")
    public ResponseEntity<List<EventVoteDTO>> getSwipeHistory(@Parameter(hidden=true) @CookieValue("jwt") String jwt,
            @PathVariable("eventId") UUID eventId) {
        String userId = JWTUtils.extractUserIdCookie(jwt, decoder);
        UUID userUuid = UUID.fromString(userId);

        List<EventVoteDTO> votes = eventService.getSwipeHistory(userUuid, eventId);
        return ResponseEntity.ok(votes);
    }

    @GetMapping("/participants/{eventId}")
    public ResponseEntity<List<String>> getParticipants(@Parameter(hidden=true) @CookieValue("jwt") String jwt, @PathVariable("eventId") UUID eventUuid) {
        String userId = JWTUtils.extractUserIdCookie(jwt, decoder);
        UUID userUuid = UUID.fromString(userId);

        List<String> participants = new ArrayList<>();
        try{
            participants = eventService.getParticipants(userUuid, eventUuid);
        } catch (NoSuchElementException e) {
            log.error("Error while getting participants", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error while getting participants", e);
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(participants);
    }
    @GetMapping("/result/{eventId}")
    public ResponseEntity<String> getResult(@Parameter(hidden = true)  @CookieValue("jwt") String jwt, @PathVariable("eventId") UUID eventUuid) {

        String userId = JWTUtils.extractUserIdCookie(jwt, decoder);
        UUID userUuid = UUID.fromString(userId);
        UUID mostVotedResult;
        try {
            mostVotedResult = eventService.getResult(eventUuid, userUuid);
            return ResponseEntity.ok(mostVotedResult.toString());
        } catch (NoSuchElementException e) {
            log.error("Error while getting result", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error while getting result", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/close/{eventId}")
    public ResponseEntity<String> closeVote(@Parameter(hidden = true)  @CookieValue("jwt") String jwt,@PathVariable("eventId") UUID eventUuid) {
        String userId = JWTUtils.extractUserIdCookie(jwt, decoder);
        UUID userUuid = UUID.fromString(userId);

        try {
            boolean isClosed = eventService.closeEvent(eventUuid, userUuid);
            if (!isClosed) {
                return ResponseEntity.badRequest().body("Event not closed");
            }
            return ResponseEntity.ok("Event closed");
        } catch (NoSuchElementException e) {
            log.error("Error while deleting event", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error while deleting event", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
