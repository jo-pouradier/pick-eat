package fr.pick_eat.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
    @ElementCollection(targetClass = URI.class, fetch = EAGER)
    private List<URI> icons;
    @Embedded
    private Day monday;
    @Embedded
    private Day tuesday;
    @Embedded
    private Day wednesday;
    @Embedded
    private Day thursday;
    @Embedded
    private Day friday;
    @Embedded
    private Day saturday;
    @Embedded
    private Day sunday;

    public List<URI> getIcons() {return icons;}

    public void setIcons(List<URI> icons) {this.icons = icons;}

    public float getRating() {return rating;}

    public void setRating(float rating) {this.rating = rating;}

    public int getNumberRatings() {return numberRatings;}

    public void setNumberRatings(int numberRatings) {this.numberRatings = numberRatings;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

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

    public void setMonday(Day monday) {this.monday = monday;}
    public void setTuesday(Day tuesday) {this.tuesday = tuesday;}
    public void setWednesday(Day wednesday) {this.wednesday = wednesday;}
    public void setThursday(Day thursday) {this.thursday = thursday;}
    public void setFriday(Day friday) {this.friday = friday;}
    public void setSaturday(Day saturday) {this.saturday = saturday;}
    public void setSunday(Day sunday) {this.sunday = sunday;}

    public Day getMonday() {return monday;}
    public Day getTuesday() {return tuesday;}
    public Day getWednesday() {return wednesday;}
    public Day getThursday() {return thursday;}
    public Day getFriday() {return friday;}
    public Day getSaturday() {return saturday;}
    public Day getSunday() {return sunday;}

    @Embeddable
    public static class Day {
        @ElementCollection(targetClass = String.class, fetch = EAGER)
        private List<String> hours;

        public List<String> getHours() {return hours;}

        public void setHours(List<String> hours) {this.hours = hours;}
    }
}
