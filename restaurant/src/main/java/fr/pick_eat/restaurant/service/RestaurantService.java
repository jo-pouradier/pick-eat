package fr.pick_eat.restaurant.service;

import fr.pick_eat.restaurant.entity.RestaurantModel;
import fr.pick_eat.restaurant.repository.RestaurantAvisRepository;
import fr.pick_eat.restaurant.repository.RestaurantNoteRepository;
import fr.pick_eat.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Objects;


public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RestaurantNoteRepository restaurantNoteRepository;
    @Autowired
    private RestaurantAvisRepository restaurantAvisRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, RestaurantNoteRepository restaurantNoteRepository, RestaurantAvisRepository restaurantAvisRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantNoteRepository = restaurantNoteRepository;
        this.restaurantAvisRepository = restaurantAvisRepository;
    }

    public parseRestaurant(JSONObject json) throws JSONException {
        if (!json.has("business_status") || !Objects.equals(json.getString("business_status"), "OPERATIONAL")){
            return null;
        }
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setName(json.getString("name"));
        restaurant.setAddress(json.getString("vicinity"));
        restaurant.setLat(json.getJSONObject("geometry").getJSONObject("location").getLong("lat"));
        restaurant.setLon(json.getJSONObject("geometry").getJSONObject("location").getLong("lon"));
        for (int i = 0; i < json.getJSONArray("types").length(); i++) {
            if (i <= json.getJSONArray("types").length() - 1) {
                restaurant.setType(json.getJSONArray("types").getString(i) + ", ");
            } else {
            restaurant.setType(json.getJSONArray("types").getString(i));
        }
        restaurant.setBucket(json.getString("icon"));

    }

}}
