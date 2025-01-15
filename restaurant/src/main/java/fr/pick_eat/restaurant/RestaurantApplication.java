package fr.pick_eat.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import fr.pick_eat.restaurant.config.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties({ RsaKeyProperties.class })
public class RestaurantApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantApplication.class, args);
	}

}
