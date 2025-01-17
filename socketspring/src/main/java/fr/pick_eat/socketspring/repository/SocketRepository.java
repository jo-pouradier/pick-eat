package fr.pick_eat.socketspring.repository;

import com.corundumstudio.socketio.SocketIOClient;
import fr.pick_eat.socketspring.model.SocketUserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public interface SocketRepository extends CrudRepository<SocketUserModel, UUID> {
    UUID findByUserId(UUID userId);

    List<SocketUserModel> getSocketUserModelByUserIdIn(Collection<UUID> userIds);

    void deleteBySocketId(UUID sessionId);
}