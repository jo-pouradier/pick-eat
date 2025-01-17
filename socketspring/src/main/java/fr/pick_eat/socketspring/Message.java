package fr.pick_eat.socketspring;

import lombok.Data;

@Data
public class Message {
    private String username;
    private String content;
    private String targetUsername;
}