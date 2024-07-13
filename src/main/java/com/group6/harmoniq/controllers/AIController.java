package com.group6.harmoniq.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@RestController
class AIController {
	private final ChatClient chatClient;

	AIController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@GetMapping("/ai")
public Map<String, Object> completion(
    @RequestParam(value = "message", defaultValue = "Give four random album names without artist's name just album name") String message, String albumName) {
	
	albumName = "Demon Days";

    message = """
        I will give album's name, your job is to give 3 other albums of other artists that are similar to the album I gave you in JSON format. I do not need the artist's name, just the album's name. Put initial album name in answer field.: 
        ["...", "...", "..."]
        """;
    
    String rawResponse = chatClient.prompt()
                                 .user(message + albumName)
                                 .call()
                                 .content();
    
    ObjectMapper objectMapper = new ObjectMapper();
    try {
        List<String> options = objectMapper.readValue(rawResponse, new TypeReference<>() {});
		String option1 = options.get(0);
        String option2 = options.get(1);
        String option3 = options.get(2);
		System.out.println(options);
        return Map.of("option1", option1, "option2", option2, "option3", option3); 
    } catch (JsonProcessingException | IndexOutOfBoundsException e) {
        // Handle JSON parsing errors gracefully 
        return Map.of("error", "Invalid JSON response"); 
    }
}
}
