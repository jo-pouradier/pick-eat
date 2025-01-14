package fr.pick_eat.auth.service;

import fr.pick_eat.auth.entity.UserBasic;
import fr.pick_eat.auth.mapper.UserMapper;
import fr.pick_eat.auth.scope.EScope;
import fr.pick_eat.auth.utils.CookiesUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final Logger logger = Logger.getLogger(CustomOAuth2UserService.class.getName());
    private AuthenticationService authenticationService;
    private final JwtService jwtService;

    @Value("${github.email-info-uri}")
    private String emailInfoUri;

    private final RestTemplate restTemplate;

    @Autowired
    public CustomOAuth2UserService(@Lazy AuthenticationService authenticationService, @Lazy JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.restTemplate = new RestTemplate();
        logger.setLevel(java.util.logging.Level.ALL);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
//        logger.info(oAuth2User.getAttributes().toString());
        // Map the GitHub user details to your custom user entity
        String email = loadEmail(userRequest);
        String name = oAuth2User.getAttribute("name");
        assert name != null;
        String firstName = name.split(" ")[0];
        String lastName = name.split(" ")[1];
        int id = oAuth2User.getAttribute("id");
        UserBasic user = new UserBasic()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPassword("github")
                .setGithubId(Long.valueOf(id));
        if (!authenticationService.isRegistered(user.getEmail())) {
            logger.info("OAUTH User not registered");
            user = authenticationService.signupGitHub(UserMapper.toRegisterUserDto(user), user.getGithubId());
            CookiesUtils.addUserCookie(UserMapper.toDto(user));
        } else {
            logger.info("OAUTH User registered");
            user = authenticationService.findByEmail(user.getEmail()).get();
            if (!user.isGitHubUser()) {
                return null;
            }
            CookiesUtils.addUserCookie(UserMapper.toDto(user));
        }
        String jwtToken = jwtService.generateToken(user, user.getUuid(), EScope.USER.getScope());
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.addCookie(jwtService.generateDeleteCookie());
        response.addCookie(jwtService.generateCookie(jwtToken));
        return oAuth2User;
    }

    private String loadEmail(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "oAuth2User cannot be null");
        String token = userRequest.getAccessToken().getTokenValue();
        RequestEntity<Void> request = RequestEntity
                .get(emailInfoUri)
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .build();
        List<Map<String, Object>> response = restTemplate.exchange(request, List.class).getBody();
        Assert.notNull(response, "Email response cannot be null");
        String email = parseEmailFromResponse(response);
        return email;
    }

    private String parseEmailFromResponse(List<Map<String, Object>> response) {
        logger.info("Got " + response.size() + " emails");
        for (Map<String, Object> email : response) {
            if (email.get("primary").equals(true)) {
                return email.get("email").toString();
            }
        }
        return "";
    }


}