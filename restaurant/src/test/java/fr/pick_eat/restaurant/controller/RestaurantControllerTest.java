package fr.pick_eat.restaurant.controller;

import fr.pick_eat.restaurant.RestaurantApplication;
import fr.pick_eat.restaurant.entity.RestaurantModel;
import fr.pick_eat.restaurant.repository.RestaurantAvisRepository;
import fr.pick_eat.restaurant.repository.RestaurantNoteRepository;
import fr.pick_eat.restaurant.repository.RestaurantRepository;
import fr.pick_eat.restaurant.service.RestaurantService;
import org.apache.tomcat.util.json.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest public class RestaurantControllerTest {

    @Autowired
    private RestaurantService restaurantService;


    @Test
    public void testGetAllRestaurants() throws IOException, JSONException, ParseException, URISyntaxException {
        // Arrange
        URI resto_path = RestaurantApplication.class.getClassLoader().getResource("all_restaurants_lyon.json").toURI();
        URI resto_details_path = RestaurantApplication.class.getClassLoader().getResource("restaurant_details.json").toURI();//        EventModel eventModel = new EventModel();
        // Act
        restaurantService.parseRestaurants(resto_path, resto_details_path);
        Iterable<RestaurantModel> restos = restaurantService.getAllRestaurants();
        // Assert
        assertNotNull(restos);
    }

}
