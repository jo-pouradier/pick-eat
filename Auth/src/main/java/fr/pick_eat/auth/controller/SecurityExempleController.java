package fr.pick_eat.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

// TODO REMOVE
@RestController
public class SecurityExempleController {
    @GetMapping("/secured")
    public String secured(Principal principal) {
        return "Hello " + principal.getName();
    }

    @GetMapping("/unsecured")
    public String secured2() {
        return "Hello unsecured";
    }


    @GetMapping("/securedScopeUser")
    public String securedScopeUser(Principal principal) {
        return "Hello " + principal.getName() + " with role user";
    }

    @GetMapping("/securedScopeGuest")
    public String securedScopeGuest(Principal principal) {
        return "Hello " + principal.getName() + " with role guest";
    }


}
