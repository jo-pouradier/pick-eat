package fr.pick_eat.socketspring;

import fr.pick_eat.socketspring.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({RsaKeyProperties.class})
public class SocketSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocketSpringApplication.class, args);
	}

}
