package fr.pick_eat.restaurant.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pick_eat.restaurant.dto.RestaurantDTO;
import fr.pick_eat.restaurant.entity.RestaurantAvisModel;
import fr.pick_eat.restaurant.entity.RestaurantModel;
import fr.pick_eat.restaurant.entity.RestaurantNoteModel;
import fr.pick_eat.restaurant.repository.RestaurantAvisRepository;
import fr.pick_eat.restaurant.repository.RestaurantNoteRepository;
import fr.pick_eat.restaurant.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.json.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;
import java.util.UUID;
// import fr.pick_eat.event.dto.EventDTO;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

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

    @Transactional
    public void parseRestaurants(String resto_path, String resto_detail_path) throws IOException, ParseException, JSONException {
        // Read the file content into a String
        String jsonContent = new String(Files.readAllBytes(Paths.get(resto_path)));
        String jsonContentDetail = new String(Files.readAllBytes(Paths.get(resto_detail_path)));


        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Iterable<RestaurantModel> places = objectMapper.readValue(jsonContent, objectMapper.getTypeFactory().constructCollectionType(List.class, RestaurantModel.class));
            List<RestaurantModel> placesList = (List<RestaurantModel>) places;
            restaurantRepository.saveAll(places);
            System.out.println(restaurantRepository.count());
        } catch (Exception e) {
            e.printStackTrace();
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
    public void test() throws JSONException, IOException, ParseException {
        String resto_path = "C:\\Users\\Adrien\\Cours\\pick-eat\\get_restaurants_data\\data\\all_restaurants_lyon.json";
        String resto_details_path = "C:\\Users\\Adrien\\Cours\\pick-eat\\get_restaurants_data\\data\\restaurant_details.json";

        // Act
        parseRestaurants(resto_path, resto_details_path);
        Iterable<RestaurantModel> restos = getAllRestaurants();
        System.out.println("restos: " + restos);

        // Assert
        assertNotNull(restos);
    }

    public static void main(String[] args) throws JSONException, IOException, ParseException {
        RestaurantService restaurantService = new RestaurantService(null, null, null);
        restaurantService.test();
    }
}
