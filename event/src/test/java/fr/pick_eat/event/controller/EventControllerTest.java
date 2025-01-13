package fr.pick_eat.event.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import fr.pick_eat.event.dto.EventDTO;
import fr.pick_eat.event.dto.EventFeedbackDTO;
import fr.pick_eat.event.dto.EventVoteDTO;
import fr.pick_eat.event.entity.EventFeedbackModel;
import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // dummy token
    private final static String TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJodWd1ZXMuZmFydGhvdWF0QHRlc3QuZnIiLCJzY29wZSI6IlVTRVIiLCJpc3MiOiJwaWNrLWVhdCIsImV4cCI6MTc0NTM5NzY4MywiaWF0IjoxNzM2NzU3NjgzLCJ1dWlkIjoiMDk1ZDE0NTktYjAwOS00MjFmLWI3YmUtMGVlZmY5NDVlYjcwIn0.H6v0J_A0Fqc2tjwl4oWdIbxbfi5O8k7cAa4cReyEixkZz7ub2ZDmJVSwDmD7dQMlA4nREsmLNtmlEkDw5GWNyczNwVN__LT-defWM6QYs5pcH72v2yEWAqAAxiwkYh6SIWOr8yKCOiMXuO-KBotwsCV8YjRoB2kLoI723YfbONC3Di2TI6LyaRG-7HnILpXPnLV7foc9zS6BSBYg3yetEyZ29KsNxaa2uZwpKHj8JMbwcfduJOvSPtIyTHlgj2XwL9TxrPKIX9-uVEK7fXr1YBNCYVFtYXe5bsQR231vNeGjCIDkxS6XVdL-nKMrv-k0tOmqY75vsQz1-doPpraGKQ";
    private final static String TOKEN2 = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJib2IuYm9iQHRlc3QuZnIiLCJzY29wZSI6IlVTRVIiLCJpc3MiOiJwaWNrLWVhdCIsImV4cCI6MTczNjg1MTIxOCwiaWF0IjoxNzM2NzY0ODE4LCJ1dWlkIjoiZWZhYjQ3NGItMTMyYS00OTMwLTk0N2MtNTc1YmNhOThmYzVmIn0.CisuTBtE9VqXG7LOByh92TWWifqzPyEnk-ROLSnXptkMg8JfjL7mNoy3Hh5kce3MX_GUf4N-Jw2LjUMPf8zztD8QlaSTE5X1mlQS5kl0LC57Hwd5kON0fFQuuz1nmxnxLZOPMaiY1nIVbUVoxUS1hCc5_Mm7gJav604ZW2rGTwSt2Eq3OJTW8AltA-tic1yJ-STz8zc7gTtzQK-qiXAigJ57BnENn6NXJvdn7RaB11ue6bDHucVMuZt37QznF8vSjbk8anGi46MzWwblw5rD7bVgtfPA_JyELJLaG8WzMcy8pO3yneveOSI0aIzn0W0EFtstRIBfKSm0xa1bJjUO5g";
    private final static Cookie COOKIE = new Cookie("jwt", TOKEN);
    private final static Cookie COOKIE2 = new Cookie("jwt", TOKEN2);

    ObjectMapper objectMapper = new ObjectMapper();
    private final EventDTO eventDTO = new EventDTO();
    private final EventFeedbackDTO feedbackDTO = new EventFeedbackDTO();

    @BeforeEach
    public void setUp() {
        eventDTO.setName("Test Event");
        eventDTO.setAddress("Test Address");
        eventDTO.setLatitude((float) 0.0);
        eventDTO.setLongitude((float) 0.0);
        eventDTO.setDate(new Date());
        eventDTO.setRadius(1000);

    }

    @Test
    public void testCreateEvent() throws Exception {
        String uuid = mockMvc.perform(MockMvcRequestBuilders.post("/create")
                .cookie(COOKIE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isOk())
                // extract content to uuid
                .andReturn().getResponse().getContentAsString();

        eventDTO.setId(UUID.fromString(uuid));
        assert (eventDTO.getId() != null);
    }

    @Test
    public void testGetHistoryNotEmpty() throws Exception {
        String historyStr = mockMvc.perform(MockMvcRequestBuilders.get("/history")
                .cookie(COOKIE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // assert that history is empty == []
        assert (!historyStr.isEmpty());
        List<?> historyList = JsonPath.read(historyStr, "$");
        assert (historyList.size() >= 0);

        // create event to get an History
        testCreateEvent();

        String historyStr2 = mockMvc.perform(MockMvcRequestBuilders.get("/history")
                .cookie(COOKIE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // assert that history is not empty and that we have the event we created before
        assert (!historyStr2.isEmpty());
        List<?> histr2List = JsonPath.read(historyStr2, "$");
        assert (historyList.size() < histr2List.size());
        String eventId = JsonPath.read(historyStr2, "$[0].id");
        // assert (UUID.fromString(eventId).equals(eventDTO.getId()));
        assert (eventId != null);
    }

    @Test
    public void testJoinEventJoinSameEvent() throws Exception {
        testCreateEvent();
        String url = String.format("/join/%s", eventDTO.getId().toString());
        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .cookie(COOKIE))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    public void testJoinEvent() throws Exception {
        testCreateEvent();

        mockMvc.perform(MockMvcRequestBuilders.post("/join/" + eventDTO.getId())
                .cookie(COOKIE2))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testSubmitFeedback() throws Exception {
        testCreateEvent();

        feedbackDTO.setId(null);
        feedbackDTO.setRating(5);
        feedbackDTO.setComment("Test Comment");
        feedbackDTO.setBucketName("Test Bucket");
        feedbackDTO.setPath("Test Path");
        feedbackDTO.setEventId(eventDTO.getId());

        assertTrue(feedbackDTO.getEventId() != null);

        String url = String.format("/feedback/%s", feedbackDTO.getEventId().toString());
        String feedbackString = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .cookie(COOKIE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedbackDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        EventFeedbackModel feedbackModel = objectMapper.readValue(feedbackString, EventFeedbackModel.class);
        assertTrue(feedbackModel.getEventId().equals(feedbackDTO.getEventId()));
        assertTrue(feedbackModel.getRating() == feedbackDTO.getRating());
        assertTrue(feedbackModel.getComment().equals(feedbackDTO.getComment()));
        assertTrue(feedbackModel.getBucketName().equals(feedbackDTO.getBucketName()));
        assertTrue(feedbackModel.getPath().equals(feedbackDTO.getPath()));

    }

    @Test
    public void testLeaveEvent() throws Exception {
        testCreateEvent();
        testJoinEvent();
        mockMvc.perform(MockMvcRequestBuilders.post("/leave/" + eventDTO.getId())
                .cookie(COOKIE2))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test void testLeaveEventOrganizer() throws Exception {
        testCreateEvent();
        mockMvc.perform(MockMvcRequestBuilders.post("/leave/" + eventDTO.getId())
                .cookie(COOKIE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSwipeEvent() throws Exception {
        testCreateEvent();

        List<EventVoteDTO> votes = new ArrayList<>();
        EventVoteDTO vote = new EventVoteDTO();
        vote.setRestaurantId(UUID.randomUUID());
        vote.setLike(true);
        votes.add(vote);

        mockMvc.perform(MockMvcRequestBuilders.post("/swipe/"+ eventDTO.getId())
                .cookie(COOKIE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(votes)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFeedbackHistory() throws Exception {
        String feedbackHistoryStr = mockMvc.perform(MockMvcRequestBuilders.get("/feedback/history")
                .cookie(COOKIE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assert (!feedbackHistoryStr.isEmpty());
        List<?> feedbackHistoryList = JsonPath.read(feedbackHistoryStr, "$");
        assert (feedbackHistoryList.size() >= 0);

        testSubmitFeedback();

        String feedbackHistoryStr2 = mockMvc.perform(MockMvcRequestBuilders.get("/feedback/history")
                .cookie(COOKIE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assert (!feedbackHistoryStr2.isEmpty());
        List<?> feedbackHistoryList2 = JsonPath.read(feedbackHistoryStr2, "$");
        assert (feedbackHistoryList.size() < feedbackHistoryList2.size());
        String uuid = JsonPath.read(feedbackHistoryStr2, "$[0].eventId");
        assert (uuid != null);
    }


}