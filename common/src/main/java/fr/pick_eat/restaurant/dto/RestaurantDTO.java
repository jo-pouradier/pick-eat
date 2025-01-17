package fr.pick_eat.restaurant.dto;

import java.net.URI;
import java.util.List;
import java.util.UUID;

public class RestaurantDTO {
    private UUID id;
    private String name;
    private String address;
    private float lat;
    private float lon;
    private List<String> types;
    private String bucket;
    private String picture; // path
    private String GeneratedPicture; // path
    private Integer price_level;
    private String description;
    private List<URI> icons;

    public UUID getId() {return id;}

    public void setId(UUID id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public float getLat() {return lat;}

    public void setLat(float lat) {this.lat = lat;}

    public float getLon() {return lon;}

    public void setLon(float lon) {this.lon = lon;}

    public List<String> getTypes() {return types;}

    public void setTypes(List<String> types) {this.types = types;}

    public String getBucket() {return bucket;}

    public void setBucket(String bucket) {this.bucket = bucket;}

    public String getPicture() {return picture;}

    public void setPicture(String picture) {this.picture = picture;}

    public String getGeneratedPicture() {return GeneratedPicture;}

    public void setGeneratedPicture(String GeneratedPicture) {this.GeneratedPicture = GeneratedPicture;}

    public Integer getPrice_level() {return price_level;}

    public void setPrice_level(Integer price_level) {this.price_level = price_level;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public List<URI> getIcons() {return icons;}

    public void setIcons(List<URI> icons) {this.icons = icons;}

}
