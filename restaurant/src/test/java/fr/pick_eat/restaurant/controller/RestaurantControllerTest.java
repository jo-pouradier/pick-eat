package fr.pick_eat.restaurant.controller;

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
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RestaurantControllerTest {

    private static final Logger log = LoggerFactory.getLogger(RestaurantControllerTest.class);
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private RestaurantAvisRepository restaurantAvisRepository;
    @Mock
    private RestaurantNoteRepository restaurantNoteRepository;

    @InjectMocks
    private RestaurantService restaurantService;


    @Test
    public void testGetAllRestaurants() throws IOException, JSONException, ParseException {
        // Arrange
        String resto_path = "C:\\Users\\Adrien\\Cours\\pick-eat\\get_restaurants_data\\data\\all_restaurants_lyon.json";
        String resto_details_path = "C:\\Users\\Adrien\\Cours\\pick-eat\\get_restaurants_data\\data\\restaurant_details.json";

        // Act
        restaurantService.parseRestaurants(resto_path, resto_details_path);
        Iterable<RestaurantModel> restos = restaurantService.getAllRestaurants();
        System.out.println("restos: " + restos);

        // Assert
        assertNotNull(restos);
    }

}
