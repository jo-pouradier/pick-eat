package fr.pick_eat.restaurant.service;


import com.fasterxml.jackson.databind.ObjectMapper;

import fr.pick_eat.event.dto.EventDTO;
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
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

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
            this.initialize();
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

    public void setRestaurantPhotos() {
        restaurantRepository.findAll().forEach(restaurant -> {
            String name = restaurant.getName();
            String photo = name.replace(" ", "_").replace("/", "_").replace("|", "_").replace("&","and");
            String photo_path = photo.replaceAll("[<>,:\"/\\\\|?*]", "_");
            restaurant.setPicture("get_restaurant_data/data/restaurants_photos/" + photo_path + ".jpg");
            restaurantRepository.save(restaurant);
        });
    }

    public List<RestaurantDTO> generateRestaurantsForEvent(EventDTO event) {
        float lat = event.getLatitude();
        float lon = event.getLongitude();
        float radius = event.getRadius();
        List<Float> coords = getAreaFromRadius(lat, lon, radius);
        return restaurantRepository.findBetweenLatAndLon(coords.get(0), coords.get(1), coords.get(2), coords.get(3)).stream().map(RestaurantMapper::toRestaurantDTO).toList();
    }

    public void updateDescriptionColumnSize() {
        restaurantRepository.updateSizeDescription();
    }

    @Transactional
    public void parseOpeningHours() throws IOException, JSONException {
        updateDescriptionColumnSize();
        // Read JSON file as String
        String content = new String(Files.readAllBytes(Paths.get("restaurant/src/main/resources/dataResto.json")));
        JSONObject jsonObject = new JSONObject(content);
        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
            String key = it.next();
            RestaurantModel resto = restaurantRepository.findByPlaceId(key);
            JSONObject restaurant = jsonObject.getJSONObject(key);
            JSONArray opening_hours = restaurant.getJSONArray("opening_hours");
            resto.setAddress(restaurant.getString("address"));
            if (restaurant.has("types")) {
                List<String> useless = List.of("restaurant", "food", "point_of_interest", "establishment");
                List<String> types = new ArrayList<>();
                JSONArray types_array = restaurant.getJSONArray("types");
                for (int i = 0; i < types_array.length(); i++) {
                    if (useless.contains(types_array.get(i))) {
                        continue;
                    }
                    types.add(types_array.getString(i));
                }
                resto.setTypes(types);
            }
            if (restaurant.has("description")) {
                resto.setDescription(restaurant.getString("description"));
            }
            resto.setRating((float) restaurant.getDouble("rating"));
            resto.setNumberRatings(restaurant.getInt("user_ratings_number"));
            for (int i = 0; i < opening_hours.length(); i++) {
                String opening = (String) opening_hours.get(i);
                if (opening.split(":")[1].replaceAll(" ", "").equals("Closed")) {
                    opening = opening.split(":")[0] + ":0:00–0:00";
                }
                if (opening.split(":")[1].replaceAll(" ", "").equals("Open24hours")) {
                    opening = opening.split(":")[0] + ":0:00–23:59";
                }
                String day = opening.split(":")[0];
                String hours = opening.replace(day.split(":")[0] + ":", "");

                List<String> valren = new ArrayList<>();
                List<String> hours_list = List.of(hours.split(","));
                for (String hour : hours_list) {
                    String time1 = hour.split("–")[0];
                    String time2 = hour.split("–")[1];
                    if (!time1.contains("PM") && !time1.contains("AM")) {
                        time1 = time1 + "PM";
                    }
                    if (!time2.contains("PM") && !time2.contains("AM")) {
                        time2 = time2 + "PM";
                    }
                    if (time1.contains("PM")) {
                        String[] time1_split = time1.split(":");
                        int t1 = (Integer.parseInt(time1_split[0].replaceAll("\\D", "").trim()) + 12)%24;
                        time1 = t1 + ":" + time1_split[1];
                    }
                    if (time2.contains("PM")) {
                        String[] time2_split = time2.split(":");
                        int t2 = (Integer.parseInt(time2_split[0].replaceAll("\\D", "").trim()) + 12)%24;
                        time2 = t2 + ":" + time2_split[1];
                    }
                    time1 = time1.replace(" ", "").replace("AM", "").replace("PM", "");
                    time2 = time2.replace(" ", "").replace("AM", "").replace("PM", "");
                    valren.add((time1 + "-" + time2).replace(" ", "").replace("\u202F", ""));
                }
                day = day.toLowerCase().trim();
                resto.getDay(day).setHours(valren);
            }
            RestaurantModel restaurantModel = restaurantRepository.save(resto);
            RestaurantModel restaurantModel1 = restaurantRepository.findById(restaurantModel.getId()).orElse(null);
            System.out.println();
        }

    }

    public List<RestaurantDTO> selectRestaurants(List<String> types, EventDTO event) {
        List<RestaurantModel> selectedRestaurants = new ArrayList<>();
        List<RestaurantModel> otherRestaurants = new ArrayList<>();

        while (selectedRestaurants.size() < 10) {
            List<RestaurantModel> restaurants = new ArrayList<>();
            List<RestaurantDTO> rest = generateRestaurantsForEvent(event);
            for (RestaurantDTO restaurant : rest) {
                System.out.println(restaurant.getName() + " " + isRestaurantOpen(restaurant.getId()));
                if (isRestaurantOpen(restaurant.getId())){
                    restaurants.add(restaurantRepository.findById(restaurant.getId()).orElseThrow());
                }
            }
            for (RestaurantModel restaurant : restaurants) {
                if (restaurant.getTypes().retainAll(types)) {
                    selectedRestaurants.add(restaurant);
                    System.out.println("more : " + selectedRestaurants.size());
                } else {
                    otherRestaurants.add(restaurant);
                }
            }
            while (selectedRestaurants.size() > 10) {
                float lowRate = 5;
                RestaurantModel lowRateRestaurant = null;
                for (RestaurantModel restaurant : selectedRestaurants) {
                    if (restaurant.getRating() < lowRate) {
                        lowRate = restaurant.getRating();
                        lowRateRestaurant = restaurant;
                    }
                }
                selectedRestaurants.remove(lowRateRestaurant);
            }
            while (selectedRestaurants.size() < 10) {
                if (otherRestaurants.isEmpty()) {
                    break;
                }
                float highRate = 0;
                RestaurantModel highRateRestaurant = null;
                for (RestaurantModel restaurant : otherRestaurants) {
                    if (restaurant.getRating() > highRate) {
                        highRate = restaurant.getRating();
                        highRateRestaurant = restaurant;
                    }
                }
                selectedRestaurants.add(highRateRestaurant);
                otherRestaurants.remove(highRateRestaurant);
                System.out.println("less : " + selectedRestaurants.size());
            }
            event.setRadius(event.getRadius() + 500);
        }
        return selectedRestaurants.stream().map(RestaurantMapper::toRestaurantDTO).toList();
    }

    public boolean isRestaurantPopular(float restaurantRating, int restaurantReviews) {
        // Averages and multiplier
        double averageRating = 4.2625375;
        double averageReviews = 481.78152;
        double popularityMultiplier = 1.2; // 20% more than average reviews

        // Determine if the restaurant is popular
        return restaurantRating >= averageRating && restaurantReviews >= (averageReviews * popularityMultiplier);
    }

    public boolean isRestaurantOpen(UUID uuid) {
        LocalTime currentTime = LocalTime.now();
        RestaurantModel restaurant = restaurantRepository.findById(uuid).orElseThrow();
        Integer day = LocalDate.now().getDayOfWeek().getValue();
        List<String> hours = restaurant.getDay(day).getHours();
        System.out.println(restaurant.getName()+hours);
        if (hours.isEmpty()) {
            return false;
        }
        for (String hour : hours) {
            System.out.println("okkkk");
            String[] time_split = hour.split("-");
            int hour1 = Integer.parseInt(time_split[0].split(":")[0].replaceAll("\\D", ""));
            int hour2 = Integer.parseInt(time_split[1].split(":")[0].replaceAll("\\D", ""));
            int minute1 = Integer.parseInt(time_split[0].split(":")[1].replaceAll("\\D", ""));
            int minute2 = Integer.parseInt(time_split[1].split(":")[1].replaceAll("\\D", ""));
            LocalTime startTime = LocalTime.of(hour1, minute1);
            LocalTime endTime = LocalTime.of(hour2, minute2);
            return currentTime.isAfter(startTime) && currentTime.isBefore(endTime);
        }
        return false;
    }

    @Transactional
    public void initialize() throws IOException, URISyntaxException, JSONException {
        URI resto_path = RestaurantApplication.class.getClassLoader().getResource("all_restaurants_lyon.json").toURI();
        URI resto_details_path = RestaurantApplication.class.getClassLoader().getResource("restaurant_details.json").toURI();
        EventDTO eventModel = new EventDTO();
        eventModel.setName("test");
        eventModel.setAddress("test");
        eventModel.setDate(new java.util.Date());
        eventModel.setLatitude(45.716972f);
        eventModel.setLongitude(4.804001f);
        eventModel.setRadius(1000);
        eventModel.setDescription("test");
        // Act
        parseRestaurants(resto_path, resto_details_path);
        setRestaurantPhotos();
        parseOpeningHours();

        UUID uuid = restaurantRepository.findByPlaceId("ChIJXxeBzmzp9EcR_dBhY9_QUXw").getId();
        RestaurantModel restaurant = restaurantRepository.findById(uuid).orElseThrow();

        System.out.println("opened : " + isRestaurantOpen(restaurantRepository.findByPlaceId("ChIJXxeBzmzp9EcR_dBhY9_QUXw").getId()));
//        assertNotNull(restos);
//        assertNotNull(resto);
    }

}
