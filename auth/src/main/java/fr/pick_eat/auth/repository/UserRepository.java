package fr.pick_eat.auth.repository;

import fr.pick_eat.auth.entity.UserBasic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserBasic, UUID> {
    Optional<UserBasic> findByEmail(String email);
}