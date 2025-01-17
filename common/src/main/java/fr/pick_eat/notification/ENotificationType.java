package fr.pick_eat.notification;

import lombok.Getter;

public enum ENotificationType {
    NEW_MESSAGE("new_message"),
    ;

    @Getter
    private final String event;

    ENotificationType(String event) {
        this.event = event;
    }
}
