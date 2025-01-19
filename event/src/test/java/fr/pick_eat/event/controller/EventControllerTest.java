package fr.pick_eat.event.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private final static String TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwic2NvcGUiOiJVU0VSIiwiaXNzIjoicGljay1lYXQiLCJleHAiOjE3NjgzODgzMDcsImlhdCI6MTczNjg1MjMwNywidXVpZCI6IjhiOGFjYTk4LWU5YjktNGFkNS1hOTAzLTc0MmU3NGVlMTViZiJ9.cp22oKCSuCwsOmCXntjx0ZKtIM6zPNF-yH-5v0q02rAK8_XcPCMHTV_u6PJkSs3djzK1TBPEjJODUPBdfBJt-h-L8XdEA-lJlgcB_wcVXSEfM-ZllIagdj9poUzGmQQSFIUy_u5OkqzP9zqOg0Sj7DEMhq8IntSHcK6rPpaQggb0N4VEgcBQ2hGvbyjYc6FsNE4p23V7FiML4P_eAFQmjT7eXY87ixLyXqBWT7-AOTqERQVN6BlPdFxC8QhJo1AXfWStcB9W2hk2Uz7sFkShk1BV0Ff0qCvYwUwXAv2Zv-B2ETLbrxqieXCkxPdi1k-G453PmMANZBpLFzuRsuOjFg";
    private final static String TOKEN2 = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0MiIsInNjb3BlIjoiVVNFUiIsImlzcyI6InBpY2stZWF0IiwiZXhwIjoxNzY4Mzg4MzUyLCJpYXQiOjE3MzY4NTIzNTIsInV1aWQiOiI3ZGZmOGRlZC1jZGQzLTRkYzUtYjhiNi00Mjg4ZjQwYTJiYmYifQ.fmICrGNVYZUCssB_iClleptwro-QOLrMUhOB5zKws_tbc-6Si_Rr99G0DEwRcc3cj4wSMFiOlSJTV3pGw-qwDMud_cuBjl39SAQDkk_T3NRRB3H2BgbVr-_v7OMLmT3GXm3rARr5bH0SJuXG6V_Xj-BidatTnaBWSnJe6FKK1fr-hhKPSF7PjVFFpsOtZIjgX4TcnkzsDr4tbrqe071OVNAh-11j8hxPafzizV36JsHjF9-DmSnWlVEQxMF9sNi_egy-10gVhOmrITW68SCB2IIeZSV1Q-gsmeHz7v3bnIr8Tu5pfesFNJ3mJFEHN9FmmodiJyYQsCYtZx-Bhx0v1g";
    private final static Cookie COOKIE = new Cookie("jwt", TOKEN);
    private final static Cookie COOKIE2 = new Cookie("jwt", TOKEN2);

    ObjectMapper objectMapper = new ObjectMapper();
    private final EventDTO eventDTO = new EventDTO();
    private final EventFeedbackDTO feedbackDTO = new EventFeedbackDTO();

    @BeforeTestClass
    public void setUp() {
        eventDTO.setName("Test Event");
        eventDTO.setAddress("Test Address");
        eventDTO.setLatitude((float) 0.0);
        eventDTO.setLongitude((float) 0.0);
        eventDTO.setDate(new Date());
        eventDTO.setRadius(1000);
        eventDTO.setDescription("Test Description");
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
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .cookie(COOKIE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testJoinEvent() throws Exception {
        testCreateEvent();

        mockMvc.perform(MockMvcRequestBuilders.get("/join/" + eventDTO.getId())
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

    @Test
    void testLeaveEventOrganizer() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders.post("/swipe/" + eventDTO.getId())
                        .cookie(COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votes)))
                .andExpect(status().isOk());
    }

    private EventVoteDTO createVote(UUID restaurantId, boolean like) {
        EventVoteDTO vote = new EventVoteDTO();
        vote.setRestaurantId(restaurantId);
        vote.setLike(like);
        return vote;
    }
    @Test
    public void testVoteResult() throws Exception{
        testCreateEvent();

        List<EventVoteDTO> votes = new ArrayList<>();
        UUID restaurant1 = UUID.randomUUID();
        UUID restaurant2 = UUID.randomUUID();
        UUID restaurant3 = UUID.randomUUID();

        // Create 2 votes for each restaurant
        votes.add(createVote(restaurant1, true));
        votes.add(createVote(restaurant1, true));
        votes.add(createVote(restaurant2, true));
        votes.add(createVote(restaurant2, false));
        votes.add(createVote(restaurant3, false));
        votes.add(createVote(restaurant3, false));

        mockMvc.perform(MockMvcRequestBuilders.post("/swipe/" + eventDTO.getId())
                        .cookie(COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votes)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/result/" + eventDTO.getId())
                        .cookie(COOKIE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(restaurant1.toString()));
    }

    @Test
    public void testCloseVote() throws Exception {
        testCreateEvent();

        mockMvc.perform(MockMvcRequestBuilders.get("/close/" + eventDTO.getId())
                        .cookie(COOKIE))
                .andExpect(status().isOk())
                .andExpect(content().string("Event closed"));
    }

    @Test
    public void testCloseVoteBadUser() throws Exception {
        testCreateEvent();

        mockMvc.perform(MockMvcRequestBuilders.get("/close/" + eventDTO.getId())
                        .cookie(COOKIE2))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Event not closed"));
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

    @Test
    public void testGetParticipants() throws Exception {
        testCreateEvent();
        String url = String.format("/participants/%s", eventDTO.getId().toString());

        String participantsStr = mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .cookie(COOKIE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assert (!participantsStr.isEmpty());
        List<?> participantsList = JsonPath.read(participantsStr, "$");
        assert (participantsList.size() == 1);
    }

    @Test
    public void testGetParticipantsNotFound() throws Exception {
        String url = String.format("/participants/%s", UUID.randomUUID().toString());

        mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .cookie(COOKIE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetParticipantsBadRequest() throws Exception {
        String invalidEventId = "invalid-uuid";
        String url = String.format("/participants/%s", invalidEventId);

        mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .cookie(COOKIE))
                .andExpect(status().isBadRequest());
    }

}