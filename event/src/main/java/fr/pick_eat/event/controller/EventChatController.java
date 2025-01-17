package fr.pick_eat.event.controller;

import fr.pick_eat.event.async.EventNotificationBrokerSender;
import fr.pick_eat.event.entity.EventChatModel;
import fr.pick_eat.event.entity.EventModel;
import fr.pick_eat.event.entity.EventParticipantModel;
import fr.pick_eat.event.mapper.EventMapper;
import fr.pick_eat.event.service.EventChatService;
import fr.pick_eat.event.service.EventService;
import fr.pick_eat.event.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
public class EventChatController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final EventService eventService;
    private final EventChatService eventChatService;
    private final EventNotificationBrokerSender eventNotificationBrokerSender;
    private final JwtDecoder jwtDecoder;

    public EventChatController(EventService eventService, EventChatService eventChatService, EventNotificationBrokerSender eventNotificationBrokerSender, JwtDecoder jwtDecoder) {
        this.eventService = eventService;
        this.eventChatService = eventChatService;
        this.eventNotificationBrokerSender = eventNotificationBrokerSender;
        this.jwtDecoder = jwtDecoder;
    }


    @PostMapping("/send_message/{eventId}")
    public ResponseEntity<EventChatModel> send_message(@CookieValue String jwt, @PathVariable("eventId") UUID eventId, @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "message", required = false) String message) {
        if (message == null && file == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message or file is required");
        }
        EventModel event = eventService.getEvent(eventId);
        if (event == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        List<UUID> participantsUuid = event.getParticipants().stream().map(EventParticipantModel::getUserId).toList();
        UUID userUuid = UUID.fromString(JWTUtils.extractUserIdCookie(jwt, jwtDecoder));
        if (participantsUuid.stream().noneMatch(uuid -> uuid.equals(userUuid))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a participant of this event");
        }
        String filePath = null;
        try {
            // Save the file to the directory
            filePath = saveImage(file, eventId);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error uploading image");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        EventChatModel chatMessage = eventChatService.send_message(eventId, userUuid, message, filePath);
        eventNotificationBrokerSender.sendEventChatDto(EventMapper.toEventChatDto(chatMessage), participantsUuid);
        return ResponseEntity.ok(chatMessage);
    }

    private String saveImage(MultipartFile file, UUID eventId) throws IOException, IllegalArgumentException {
        Path uploadPath = Paths.get(uploadDir + "/event/" + eventId);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String contentType = file.getContentType();
        String[] allowedTypes = {"image/jpeg", "image/png", "image/jpg", "image/gif"};
        if (!List.of(allowedTypes).contains(contentType)) {
            throw new IllegalArgumentException("Only JPEG, PNG, JPG and GIF files are allowed");
        }
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    @GetMapping("/get_messages/{eventId}")
    public ResponseEntity<List<EventChatModel>> get_messages(@CookieValue("jwt") String jwt, @PathVariable("eventId") UUID eventId) {
        EventModel event = eventService.getEvent(eventId);
        if (event == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        List<UUID> participantsUuid = event.getParticipants().stream().map(EventParticipantModel::getUserId).toList();
        UUID userUuid = UUID.fromString(JWTUtils.extractUserIdCookie(jwt, jwtDecoder));
        if (participantsUuid.stream().noneMatch(uuid -> uuid.equals(userUuid))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a participant of this event");
        }

        List<EventChatModel> messages = eventChatService.get_messages(eventId);
        return ResponseEntity.ok(messages);
    }
}
