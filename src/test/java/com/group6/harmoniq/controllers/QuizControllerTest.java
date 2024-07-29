package com.group6.harmoniq.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import io.github.cdimascio.dotenv.Dotenv;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;

import jakarta.servlet.http.HttpSession;

import java.util.*;

import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
  "SPOTIFY_CLIENT_ID=TestClientId",
     "SPOTIFY_CLIENT_SECRET=TestClientSecret",
     "SPOTIFY_REDIRECT_URI=TestRedirectURI"
 })
@AutoConfigureMockMvc
public class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private List<Map<String, Object>> recognitionQuizAnswers;

    @BeforeAll
    static void loadEnv() {
      Dotenv dotenv = Dotenv.configure().filename(".env.test").load();
          dotenv.entries().forEach(entry -> {
          System.setProperty(entry.getKey(), entry.getValue());
         });
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);       
    }

    @Test
    void testStartRecognitionQuiz() throws Exception {

          List<Map<String, Object>> items = new ArrayList<>();
        
          Map<String, Object> track1 = new HashMap<>();
          track1.put("name", "Track 1");

          //Add 50 tracks to items to mimic the 50 tracks in the Spotify response
          for (int i = 0; i < 50; i++) {
            items.add(track1);
          }
      
          Map<String, Object> responseBody = new HashMap<>();
          responseBody.put("items", items);
  
          ResponseEntity<Map> responseEntity = ResponseEntity.ok(responseBody);
  
          String getTop50TracksUrl = "https://api.spotify.com/v1/me/top/tracks?limit=50";

          when(restTemplate.exchange(
                  eq(getTop50TracksUrl), eq(HttpMethod.GET),
                  any(HttpEntity.class), eq(Map.class)
          )).thenReturn(responseEntity);
  
          HttpSession session = mock(HttpSession.class);
          when(session.getAttribute("accessToken")).thenReturn("testAccessToken");
  
          mockMvc.perform(get("/quizzes/recognitionQuiz"))
                  .andExpect(status().is3xxRedirection())
                  .andExpect(view().name("redirect:/quizzes/recognitionQuiz/0"));
    }

    @Test
    void testGetRecognitionQuizQuestion() throws Exception {
           
             List<Map<String, Object>> items = new ArrayList<>();
             Map<String, Object> track1 = new HashMap<>();
             track1.put("name", "Track 1");
             
             for (int i = 1; i <= 50; i++) {
                items.add(track1);
             }    

        ResponseEntity<Map> mockResponse = mock(ResponseEntity.class);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("items", items);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(Map.class)))
        .thenReturn(mockResponse);
        when(mockResponse.getBody()).thenReturn(responseBody);

        List<Map<String, Object>> recognitionQuizAnswers = new ArrayList<>();
   
        // Add the first 5 tracks from items into answers
        recognitionQuizAnswers = new ArrayList<>(items.subList(0, 4)); 
  
        mockMvc.perform(get("/quizzes/recognitionQuiz/5")
        .sessionAttr("recognitionQuizAnswers", recognitionQuizAnswers))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("score"))
                .andExpect(model().attribute("score", 0))
                .andExpect(view().name("quizzes/quizResult"));
    }

    @Test
    void testProcessRecogntionQuizIncorrectAnswer() throws Exception {
      
      Map<String, Object> mockAnswer = new HashMap<>();
      mockAnswer.put("name", "Correct Answer");
      
    when(recognitionQuizAnswers.get(0)).thenReturn(mockAnswer);

      mockMvc.perform(post("/quizzes/recognitionQuiz/submit")
      .param("selectedOption", "Incorrect Answer")
      .param("questionId", "0"))
      .andExpect(model().attributeExists("result"))
      .andExpect(model().attribute("result", "Incorrect."))
      .andExpect(model().attributeExists("score"))
      .andExpect(model().attribute("score", 0))
      .andExpect(view().name("quizzes/quizResult"));
    }
    @Test
    void testProcessRecogntionQuizCorrectAnswer() throws Exception {
      
      Map<String, Object> mockAnswer = new HashMap<>();
      mockAnswer.put("name", "Correct Answer");
      
      when(recognitionQuizAnswers.get(0)).thenReturn(mockAnswer);

      mockMvc.perform(post("/quizzes/recognitionQuiz/submit")
      .param("selectedOption", "Correct Answer")
      .param("questionId", "0"))
      .andExpect(model().attributeExists("result"))
      .andExpect(model().attribute("result", "Correct!"))
      .andExpect(model().attributeExists("score"))
      .andExpect(model().attribute("score", 1))
      .andExpect(view().name("quizzes/quizResult"));
    }

}
