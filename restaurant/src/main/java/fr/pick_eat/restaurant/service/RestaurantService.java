package fr.pick_eat.restaurant.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pick_eat.event.dto.EventDTO;
import fr.pick_eat.restaurant.dto.RestaurantDTO;
import fr.pick_eat.event.entity.EventModel;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static fr.pick_eat.restaurant.utils.CoordCalcul.getAreaFromRadius;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;


@Service
public class RestaurantService {
    @Autowired
    private final RestaurantRepository restaurantRepository;
    @Autowired
    private final RestaurantNoteRepository restaurantNoteRepository;
    @Autowired
    private final RestaurantAvisRepository restaurantAvisRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, RestaurantNoteRepository restaurantNoteRepository, RestaurantAvisRepository restaurantAvisRepository) throws JSONException, IOException, ParseException {
        this.restaurantRepository = restaurantRepository;
        this.restaurantNoteRepository = restaurantNoteRepository;
        this.restaurantAvisRepository = restaurantAvisRepository;
        this.test();
    }


    @Transactional
    public void parseRestaurants(String resto_path, String resto_detail_path) throws IOException, ParseException, JSONException {
        // Read the file content into a String
        String jsonContent = new String(Files.readAllBytes(Paths.get(resto_path)));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Iterable<RestaurantModel> places = objectMapper.readValue(jsonContent, objectMapper.getTypeFactory().constructCollectionType(List.class, RestaurantModel.class));
            restaurantRepository.saveAll(places);
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


    public List<RestaurantDTO> generateRestaurantsForEvent(EventDTO event) {
        float lat = event.getLatitude();
        float lon = event.getLongitude();
        float radius = event.getRadius();
        List<Float> coords = getAreaFromRadius(lat, lon, radius);
        return RestaurantDTO.fromEntityAll(restaurantRepository.findBetweenLatAndLon(coords.get(0), coords.get(1), coords.get(2), coords.get(3)));
    }


    public void test() throws JSONException, IOException, ParseException {
        String resto_path = "get_restaurants_data\\data\\all_restaurants_lyon.json";
        String resto_details_path = "pick-eat\\get_restaurants_data\\data\\restaurant_details.json";
//        EventModel eventModel = new EventModel();
//        eventModel.setName("test");
//        eventModel.setAddress("test");
//        eventModel.setDate(new java.util.Date());
//        eventModel.setLatitude(45.716972f);
//        eventModel.setLongitude(4.804001f);
//        eventModel.setRadius(1000);
//        EventDTO event = EventDTO.fromEntity(eventModel);

        // Act
        parseRestaurants(resto_path, resto_details_path);
//        Iterable<RestaurantModel> restos = getAllRestaurants();

//        List<RestaurantDTO> resto = generateRestaurantsForEvent(event);
//        System.out.println("generated restaurants : " + resto);
//
//        // Assert
//        assertNotNull(restos);
//        assertNotNull(resto);
    }

}
