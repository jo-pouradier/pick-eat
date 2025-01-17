package fr.pick_eat.event.controller;

import fr.pick_eat.event.async.EventNotificationBrokerSender;
import fr.pick_eat.event.dto.EventChatDto;
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
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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


    @PostMapping("/{eventId}/message")
    public ResponseEntity<EventChatModel> send_message(@CookieValue(value = "jwt") String jwt, @PathVariable("eventId") UUID eventId, @RequestParam(value = "file", required = false) String base64File, @RequestParam(value = "message", required = false) String message) {
        if ((message == null || message.isBlank()) && base64File == null) {
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
        if (base64File != null) {
            try {
                filePath = saveImage(base64File, eventId);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error uploading image");
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
        EventChatModel chatMessage = eventChatService.sendMessage(eventId, userUuid, message, filePath, participantsUuid);
        return ResponseEntity.ok(chatMessage);
    }

    private String saveImage(String base64File, UUID eventId) throws IOException, IllegalArgumentException {
        Path uploadPath = Paths.get(uploadDir, "event", eventId.toString());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String[] parts = base64File.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid base64 file format");
        }
        String metadata = parts[0];
        String base64Data = parts[1];
        String contentType = metadata.split(";")[0].split(":")[1];
        String[] allowedTypes = {"image/jpeg", "image/png", "image/jpg", "image/gif"};
        if (!List.of(allowedTypes).contains(contentType)) {
            throw new IllegalArgumentException("Only JPEG, PNG, JPG and GIF files are allowed");
        }
        byte[] fileData = java.util.Base64.getDecoder().decode(base64Data);
        String fileName = UUID.randomUUID() + "." + contentType.split("/")[1];
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, fileData);

        return "/assets/event/" + eventId + "/" + fileName;
    }

    @GetMapping("/{eventId}/messages")
    public ResponseEntity<List<EventChatDto>> get_messages(@CookieValue("jwt") String jwt, @PathVariable("eventId") UUID eventId) {
        EventModel event = eventService.getEvent(eventId);
        if (event == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        List<UUID> participantsUuid = event.getParticipants().stream().map(EventParticipantModel::getUserId).toList();
        UUID userUuid = UUID.fromString(JWTUtils.extractUserIdCookie(jwt, jwtDecoder));
        if (participantsUuid.stream().noneMatch(uuid -> uuid.equals(userUuid))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a participant of this event");
        }

        List<EventChatDto> messages = eventChatService.getMessages(eventId).stream().map(EventMapper::toEventChatDto).toList();
        return ResponseEntity.ok(messages);
    }
}
