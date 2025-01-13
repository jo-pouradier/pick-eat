package fr.pick_eat.restaurant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.Generated;

import java.util.UUID;

@Entity
public class RestaurantNoteModel {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private UUID id;
    private UUID restaurantId;
    private Integer note;
    private UUID userId;

    public RestaurantNoteModel() {}

    public RestaurantNoteModel(UUID restaurantId, Integer note) {
        this.id = UUID.randomUUID();
        this.restaurantId = restaurantId;
        this.note = note;
    }

    public UUID getId() {return id;}

    public void setId(UUID id) {this.id = id;}

    public UUID getRestaurantId() {return restaurantId;}

    public void setRestaurantId(UUID restaurantId) {this.restaurantId = restaurantId;}

    public Integer getNote() {return note;}

    public void setNote(Integer note) {this.note = note;}

    public UUID getUserId() {return userId;}

    public void setUserId(UUID userId) {this.userId = userId;}
}
