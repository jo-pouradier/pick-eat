package fr.pick_eat.auth.controller;

import com.nimbusds.jose.shaded.gson.Gson;
import fr.pick_eat.auth.dto.LoginUserDto;
import fr.pick_eat.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private final Gson gson = new Gson();


    private final String registerDto = "{\"email\": \"bob.bob@cpe.fr\"," + " \"password\": \"azerty\"," + " \"firstName\": \"Bob\", " + "\"lastName\": \"BOBY\" " + "}";
    private final LoginUserDto loginUserDto = new LoginUserDto("bob.bob@cpe.fr", "azerty");
    private final LoginUserDto loginUserDtoWrongEmail = new LoginUserDto("alice.bob@cpe.ff", "azerty");
    private final LoginUserDto loginUserDtoWrongPassword = new LoginUserDto("bob.bob@cpe.fr", "azertyuiop");

    @BeforeEach
    public void setUp() {
        clearDatabase();
    }

    @Test
    public void testRegister() throws Exception {
        register().andExpect(status().isOk());
        register().andExpect(status().is4xxClientError()).andExpect(result -> result.getResponse().getContentAsString().contains("User already exists"));
    }

    @Test
    public void testLogin() throws Exception {
        register().andExpect(status().isOk());
        login().andExpect(status().isOk());
    }

    @Test
    public void testLoginWithWrongPassword() throws Exception {
        register().andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(loginUserDtoWrongPassword))).andExpect(status().is4xxClientError()).andReturn();
    }

    @Test
    public void testLoginWithWrongEmail() throws Exception {
        register().andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(loginUserDtoWrongEmail))).andExpect(status().is4xxClientError()).andReturn();
    }

    private ResultActions login() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(loginUserDto)));
    }

    private ResultActions register() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(registerDto));
    }

    private void clearDatabase() {
        userRepository.deleteAll();
    }
}
