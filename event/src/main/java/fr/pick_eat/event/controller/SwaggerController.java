package fr.pick_eat.event.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
public class SwaggerController {
    
    @GetMapping("/")
    public RedirectView index() {
        // redirect to swagger-ui.html
        return new RedirectView("/swagger-ui/index.html");
    }
}
