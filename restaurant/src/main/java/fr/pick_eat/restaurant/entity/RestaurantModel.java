package fr.pick_eat.restaurant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Generated;
import org.springframework.boot.configurationprocessor.json.JSONArray;

import java.util.List;
import java.util.UUID;

@Entity
public class RestaurantModel {
    @Id
    @Generated
    private UUID id;
    private String name;
    private String address;
    private Double lat;
    private Double  lon;
    private String type;
    private String bucket;
    private String picture; // path
    private String GeneratedPicture; // path
    private Integer price_level;

    public RestaurantModel() {
        this.id = UUID.randomUUID();
    }

    public RestaurantModel(String name, String address, Double lat, Double lon, String type, String bucket, String picture, String GeneratedPicture, Integer price_level) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.type = type;
        this.bucket = bucket;
        this.picture = picture;
        this.GeneratedPicture = GeneratedPicture;
        this.price_level = price_level;
    }

    public UUID getId() {return id;}

    public void setId(UUID id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public String getBucket() {return bucket;}

    public void setBucket(String bucket) {this.bucket = bucket;}

    public String getPicture() {return picture;}

    public void setPicture(String picture) {this.picture = picture;}

    public String getGeneratedPicture() {return GeneratedPicture;}

    public void setGeneratedPicture(String GeneratedPicture) {this.GeneratedPicture = GeneratedPicture;}

    public double getLat() {return lat;}

    public void setLat(double lat) {this.lat = lat;}

    public double getLon() {return lon;}

    public void setLon(double lon) {this.lon = lon;}

    public Integer getPrice_level() {return price_level;}

    public void setPrice_level(Integer price_level) {this.price_level = price_level;}
}
