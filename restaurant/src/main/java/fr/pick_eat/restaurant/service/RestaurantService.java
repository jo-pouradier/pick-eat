package fr.pick_eat.restaurant.service;

import fr.pick_eat.restaurant.dto.RestaurantDTO;
import fr.pick_eat.restaurant.entity.RestaurantAvisModel;
import fr.pick_eat.restaurant.entity.RestaurantModel;
import fr.pick_eat.restaurant.entity.RestaurantNoteModel;
import fr.pick_eat.restaurant.repository.RestaurantAvisRepository;
import fr.pick_eat.restaurant.repository.RestaurantNoteRepository;
import fr.pick_eat.restaurant.repository.RestaurantRepository;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
// import fr.pick_eat.event.dto.EventDTO;

import static fr.pick_eat.restaurant.utils.CoordCalcul.getAreaFromRadius;


@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantNoteRepository restaurantNoteRepository;
    private final RestaurantAvisRepository restaurantAvisRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, RestaurantNoteRepository restaurantNoteRepository, RestaurantAvisRepository restaurantAvisRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantNoteRepository = restaurantNoteRepository;
        this.restaurantAvisRepository = restaurantAvisRepository;
    }

    public RestaurantModel parseRestaurantGeneral(JSONObject json) throws JSONException {
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
        }}
        restaurant.setBucket(json.getString("icon"));
        restaurant.setPrice_level(json.has("price_level") ? json.getInt("price_level") : null);
        setRestaurantNoteModel(restaurant.getId(), Integer.valueOf(json.getString("rating")));
        return restaurant;
    }

    public String parseRestaurantDetails(JSONObject json, Integer place_id) throws JSONException {
        return json.getJSONObject(place_id.toString()).getString("formatted_address");
    }

    public void parseRestaurants(String resto_path, String resto_detail_path) throws FileNotFoundException, ParseException, JSONException {
        JSONParser parser = new JSONParser(new FileReader(resto_path));
        JSONParser parser2 = new JSONParser(new FileReader(resto_detail_path));
        LinkedHashMap<String, Object> json = parser.parseObject();
        LinkedHashMap<String, Object> json2 = parser2.parseObject();
        for (String key : json.keySet()) {
            JSONObject restaurant = new JSONObject((LinkedHashMap<String, Object>) json.get(key));
            RestaurantModel restaurantModel = parseRestaurantGeneral(restaurant);
            if (restaurantModel != null) {
                restaurantModel.setAddress(parseRestaurantDetails(new JSONObject(json2), Integer.parseInt(key)));
                saveRestaurant(restaurantModel);
            }
        }

    }

    public void setRestaurantNoteModel(UUID restaurantId, Integer note) {
        RestaurantNoteModel restaurantNoteModel = new RestaurantNoteModel();
        restaurantNoteModel.setRestaurantId(restaurantId);
        restaurantNoteModel.setNote(note);
        restaurantNoteRepository.save(restaurantNoteModel);
    }


    public void saveRestaurant(RestaurantModel restaurant) {
        restaurantRepository.save(restaurant);
    }

    public void deleteRestaurant(RestaurantModel restaurant) {
        restaurantRepository.delete(restaurant);
    }

    public Iterable<RestaurantModel> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public void commentRestaurant(UUID restaurantId, String comment, UUID userId) {
        RestaurantAvisModel restaurantAvisModel = new RestaurantAvisModel();
        restaurantAvisModel.setRestaurantId(restaurantId);
        restaurantAvisModel.setComment(comment);
        if (userId != null) {
            restaurantAvisModel.setUserId(userId);
        }
        restaurantAvisRepository.save(restaurantAvisModel);
    }

    public void noteRestaurant(UUID restaurantId, Integer note, UUID userId) {
        RestaurantNoteModel restaurantNoteModel = new RestaurantNoteModel();
        restaurantNoteModel.setRestaurantId(restaurantId);
        restaurantNoteModel.setNote(note);
        if (userId != null) {
            restaurantNoteModel.setUserId(userId);
        }
        restaurantNoteRepository.save(restaurantNoteModel);
    }

    public String getRestaurant(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId).toString();
    }


//  public List<RestaurantDTO> generateRestaurantsForEvent(@NonNull EventDTO event) {
//    double lat = event.getLat();
//    double lon = event.getLon();
//    double radius = event.getRadius();
//    List<Double> coords = getAreaFromRadius(lat, lon, radius);
//    double minLat = coords.get(0);
//    double maxLat = coords.get(1);
//    double minLon = coords.get(2);
//    double maxLon = coords.get(3);
//    return restaurantRepository.findBetweenLatAndLon(minLat, maxLat, minLon, maxLon);
//    }
}
