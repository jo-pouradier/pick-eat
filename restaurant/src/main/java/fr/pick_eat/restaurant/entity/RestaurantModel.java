package fr.pick_eat.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantModel {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private UUID id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("vicinity")
    private String address;
    @JsonProperty("geometry.location.lat")
    private double lat;
    @JsonProperty("geometry.location.lng")
    private double lon;
    private String type;
    @JsonProperty("icon")
    private String bucket;
    private String picture; // path
    private String GeneratedPicture; // path
    @JsonProperty("price_level")
    private int price_level;
    @JsonProperty("place_id")
    private String place_id;


    public RestaurantModel() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_id() {return place_id;}

    public void setPlace_id(String place_id) {this.place_id = place_id;}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getGeneratedPicture() {
        return GeneratedPicture;
    }

    public void setGeneratedPicture(String GeneratedPicture) {
        this.GeneratedPicture = GeneratedPicture;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Integer getPrice_level() {
        return price_level;
    }

    public void setPrice_level(Integer price_level) {
        this.price_level = price_level;
    }
}
