package fr.pick_eat.auth.controller;

import com.nimbusds.jose.shaded.gson.Gson;
import fr.pick_eat.auth.dto.GuestUserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GuestAuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final Gson gson = new Gson();

    private final GuestUserDto guestUserDto = new GuestUserDto(UUID.randomUUID(), "Bob", "BOBY");

    @Test
    public void testLogin() throws Exception {
        loginGuest().andExpect(status().isOk());
    }

    private ResultActions loginGuest() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/guest/login").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(guestUserDto)));
    }
}