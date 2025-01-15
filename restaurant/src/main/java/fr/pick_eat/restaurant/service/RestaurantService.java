package fr.pick_eat.restaurant.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import dto.EventDTO;
import fr.pick_eat.restaurant.RestaurantApplication;
import fr.pick_eat.restaurant.dto.RestaurantAvisDTO;
import fr.pick_eat.restaurant.dto.RestaurantDTO;
import fr.pick_eat.restaurant.entity.RestaurantAvisModel;
import fr.pick_eat.restaurant.entity.RestaurantModel;
import fr.pick_eat.restaurant.entity.RestaurantNoteModel;
import fr.pick_eat.restaurant.mapper.RestaurantMapper;
import fr.pick_eat.restaurant.repository.RestaurantAvisRepository;
import fr.pick_eat.restaurant.repository.RestaurantNoteRepository;
import fr.pick_eat.restaurant.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static fr.pick_eat.restaurant.utils.CoordCalcul.getAreaFromRadius;


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
        try {
            this.test();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    @Transactional
    public void parseRestaurants(URI resto_path, URI resto_detail_path) throws IOException {
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

    public List<RestaurantAvisDTO> getComments(UUID restaurantId) {
        return restaurantAvisRepository.findByRestaurantId(restaurantId).stream().map(RestaurantMapper::toRestaurantAvisDTO).toList();
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

    public RestaurantModel getRestaurant(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow();
    }

    public String retrieveRestaurantPhoto(UUID restaurantId) {
        String name = restaurantRepository.findById(restaurantId).orElseThrow().getName();
        String photo = name.replace(" ", "_").replace("/", "_").replace("|", "_").replace("&","and");
        return photo;
    }

    public List<RestaurantDTO> generateRestaurantsForEvent(EventDTO event) {
        float lat = event.getLatitude();
        float lon = event.getLongitude();
        float radius = event.getRadius();
        List<Float> coords = getAreaFromRadius(lat, lon, radius);
        return restaurantRepository.findBetweenLatAndLon(coords.get(0), coords.get(1), coords.get(2), coords.get(3)).stream().map(RestaurantMapper::toRestaurantDTO).toList();
    }


    public void test() throws JSONException, IOException, ParseException, URISyntaxException {
        URI resto_path = RestaurantApplication.class.getClassLoader().getResource("all_restaurants_lyon.json").toURI();
        URI resto_details_path = RestaurantApplication.class.getClassLoader().getResource("restaurant_details.json").toURI();//        EventModel eventModel = new EventModel();
//        eventModel.setName("test");
//        eventModel.setAddress("test");
//        eventModel.setDate(new java.util.Date());
//        eventModel.setLatitude(45.716972f);
//        eventModel.setLongitude(4.804001f);
//        eventModel.setRadius(1000);
//        EventDTO event = EventDTO.fromEntity(eventModel);
        // Act
        parseRestaurants(resto_path, resto_details_path);

//        List<RestaurantDTO> resto = generateRestaurantsForEvent(event);
//        System.out.println("generated restaurants : " + resto);
//
//        // Assert
//        assertNotNull(restos);
//        assertNotNull(resto);
    }

}
