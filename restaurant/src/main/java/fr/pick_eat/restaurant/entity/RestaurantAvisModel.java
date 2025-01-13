package fr.pick_eat.restaurant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class RestaurantAvisModel {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private UUID id;
    private UUID restaurantId;
    private String comment;
    private UUID userId;

    public UUID getId() {return id;}

    public void setId(UUID id) {this.id = id;}

    public UUID getRestaurantId() {return restaurantId;}

    public void setRestaurantId(UUID restaurantId) {this.restaurantId = restaurantId;}

    public String getComment() {return comment;}

    public void setComment(String comment) {this.comment = comment;}

    public UUID getUserId() {return userId;}

    public void setUserId(UUID userId) {this.userId = userId;}
}
