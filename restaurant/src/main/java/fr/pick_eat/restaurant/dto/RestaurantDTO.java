package fr.pick_eat.restaurant.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RestaurantDTO {
    private UUID id;
    private String name;
    private String address;
    private float lat;
    private float lon;
    private String type;
    private String bucket;
    private String picture; // path
    private String GeneratedPicture; // path
    private Integer price_level;

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

    public float getLat() {return lat;}

    public void setLat(float lat) {this.lat = lat;}

    public float getLon() {return lon;}

    public void setLon(float lon) {this.lon = lon;}

    public Integer getPrice_level() {return price_level;}

    public void setPrice_level(Integer price_level) {this.price_level = price_level;}

}
