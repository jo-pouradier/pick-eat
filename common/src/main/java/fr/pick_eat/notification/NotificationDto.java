package fr.pick_eat.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public abstract class NotificationDto<T> {
    private ENotificationType type;
    private List<UUID> destination;
    protected T content;
}
