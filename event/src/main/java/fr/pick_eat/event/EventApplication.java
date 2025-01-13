package fr.pick_eat.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Event Micro Service", version = "1.0", description = "Documentation Event Micro Service"), security = {
		@SecurityRequirement(name = "bearerAuth")
})
@EnableConfigurationProperties({fr.pick_eat.event.config.RsaKeyProperties.class})
public class EventApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventApplication.class, args);
	}

}
