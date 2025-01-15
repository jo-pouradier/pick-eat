package fr.pick_eat.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pick_eat.auth.dto.LoginUserDto;
import fr.pick_eat.auth.dto.RegisterUserDto;
import fr.pick_eat.auth.dto.UserBasicDto;
import fr.pick_eat.auth.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private final RegisterUserDto registerDto1 = new RegisterUserDto()
            .setEmail("test1@test.fr")
            .setPassword("password1")
            .setFirstName("test1")
            .setLastName("test1");

    private final RegisterUserDto registerDto2 = new RegisterUserDto()
            .setEmail("test2@test.fr")
            .setPassword("password2")
            .setFirstName("test2")
            .setLastName("test2");

    private final LoginUserDto loginUserDto = new LoginUserDto("test1@test.fr", "password1");
    private final LoginUserDto loginUserDtoWrongEmail = new LoginUserDto("test2@test.fr", "azerty");
    private final LoginUserDto loginUserDtoWrongPassword = new LoginUserDto("test1@test.fr", "password2");

    @BeforeEach
    public void setUp() {
        clearDatabase();
    }

    @Test
    public void testRegister() throws Exception {
        register(registerDto1).andExpect(status().isOk());
        register(registerDto1).andExpect(status().is4xxClientError()).andExpect(result -> result.getResponse().getContentAsString().contains("User already exists"));
    }

    @Test
    public void testLogin() throws Exception {
        register(registerDto1).andExpect(status().isOk());
        login(loginUserDto).andExpect(status().isOk());
    }

    @Test
    public void testLoginWithWrongPassword() throws Exception {
        register(registerDto1).andExpect(status().isOk());
        login(loginUserDtoWrongPassword).andExpect(status().is4xxClientError()).andReturn();
    }

    @Test
    public void testLoginWithWrongEmail() throws Exception {
        register(registerDto1).andExpect(status().isOk());
        login(loginUserDtoWrongEmail).andExpect(status().is4xxClientError()).andReturn();
    }

    @Test
    void getUserById() throws Exception {
        ResultActions result = register(registerDto1);
        List<Cookie> cookies = Arrays.stream(result.andReturn().getResponse().getCookies()).filter(cookie -> cookie.getMaxAge() > 0).toList();
        UserBasicDto user = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), UserBasicDto.class);
        result = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", user.getUuid()).contentType(MediaType.APPLICATION_JSON).cookie(cookies.toArray(new Cookie[0])));
        UserBasicDto userGet = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), UserBasicDto.class);
        assert user.equals(userGet);
    }

    @Test
    void getUsersByIds() throws Exception {
        List<UUID> uuids = new ArrayList<>();
        ResultActions result = register(registerDto1);
        UserBasicDto user = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), UserBasicDto.class);
        uuids.add(user.getUuid());
        result = register(registerDto2);
        user = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), UserBasicDto.class);
        uuids.add(user.getUuid());
        List<Cookie> cookies = Arrays.stream(result.andReturn().getResponse().getCookies()).filter(cookie -> cookie.getMaxAge() > 0).toList();
        result = mockMvc.perform(MockMvcRequestBuilders.post("/users/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(uuids)).cookie(cookies.toArray(new Cookie[0])));
        List<UserBasicDto> users = Arrays.asList(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), UserBasicDto[].class));
        assert users.size() == 2;
        assert users.stream().map(UserBasicDto::getUuid).toList().containsAll(uuids);
    }

    private ResultActions login(LoginUserDto loginUserDto) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginUserDto)));
    }

    private ResultActions register(RegisterUserDto registerDto) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerDto)));
    }

    private void clearDatabase() {
        userRepository.deleteAll();
    }

}
