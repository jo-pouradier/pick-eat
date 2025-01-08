package fr.pick_eat.restaurant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Generated;

import java.util.UUID;

@Entity
public class RestaurantModel {
    @Id
    @Generated
    private UUID id;
    private String name;
    private String address;
    private long lat;
    private long  lon;
    private String type;
    private String bucket;
    private String picture; // path
    private String GeneratedPicture; // path
    private Integer price_level;

    public RestaurantModel() {}

    public RestaurantModel(String name, String address, String type, String bucket, String picture, String GeneratedPicture, long lat, long lon) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.type = type;
        this.bucket = bucket;
        this.picture = picture;
        this.GeneratedPicture = GeneratedPicture;
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

    public long getLat() {return lat;}

    public void setLat(long lat) {this.lat = lat;}

    public long getLon() {return lon;}

    public void setLon(long lon) {this.lon = lon;}

    public Integer getPrice_level() {return price_level;}

    public void setPrice_level(Integer price_level) {this.price_level = price_level;}
}
