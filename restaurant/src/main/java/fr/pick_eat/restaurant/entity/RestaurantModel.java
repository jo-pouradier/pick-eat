package fr.pick_eat.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "restaurant")
public class RestaurantModel {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private UUID id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("address")
    private String address;
    private float lat;
    private float lon;
    @ElementCollection(targetClass = String.class, fetch = EAGER)
    @JsonProperty("types")
    private List<String> types;
    private String bucket;
    private String picture; // path
    private String GeneratedPicture; // path
    @JsonProperty("price_level")
    private int price_level;
    @JsonProperty("place_id")
    private String place_id;
    private String description;
    private float rating;
    private int numberRatings;
    @ElementCollection(targetClass = String.class, fetch = EAGER)
    private List<String> icons;
    @ElementCollection(targetClass = Day.class, fetch = EAGER)
    private List<Day> days = List.of(new Day(), new Day(), new Day(), new Day(), new Day(), new Day(), new Day());

    public List<String> getIcons() {
        return icons;
    }

    public void setIcons(List<String> icons) {
        this.icons = icons;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNumberRatings() {
        return numberRatings;
    }

    public void setNumberRatings(int numberRatings) {
        this.numberRatings = numberRatings;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
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

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public Integer getPrice_level() {
        return price_level;
    }

    public void setPrice_level(Integer price_level) {
        this.price_level = price_level;
    }

    @JsonProperty("geometry")
    public void setGeometry(Geometry geometry) {
        if (geometry != null && geometry.getLocation() != null) {
            this.lat = geometry.getLocation().getLat();
            this.lon = geometry.getLocation().getLng();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Geometry {
        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    public static class Location {
        private float lat;
        private float lng;

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    @JsonIgnore
    public Day getDay(int day) {
        day = day - 1;
        if (days != null && days.size() > day) {
            return days.get(day);
        }
        return null;
    }

    @JsonIgnore
    public Day getDay(String dayString) {
        int day = switch (dayString) {
            case "monday" -> 0;
            case "tuesday" -> 1;
            case "wednesday" -> 2;
            case "thursday" -> 3;
            case "friday" -> 4;
            case "saturday" -> 5;
            case "sunday" -> 6;
            default -> 7;
        };
        if (days != null && days.size() > day) {
            return days.get(day);
        }
        return null;
    }

    public static class Day implements Serializable {
        @ElementCollection(targetClass = String.class, fetch = EAGER)
        private List<String> hours;

        public List<String> getHours() {
            return hours;
        }

        public void setHours(List<String> hours) {
            this.hours = hours;
        }
    }
}
