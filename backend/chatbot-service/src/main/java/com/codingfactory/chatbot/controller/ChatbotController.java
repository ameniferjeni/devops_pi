package com.codingfactory.chatbot.controller;

import com.codingfactory.chatbot.dto.ChatbotRequestDto;
import com.codingfactory.chatbot.dto.ChatbotResponseDto;
import com.codingfactory.chatbot.dto.ServiceConsultingSummaryDto;
import com.codingfactory.chatbot.service.ChatbotEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*")
public class ChatbotController {

    private final ChatbotEngineService chatbotEngineService;

    public ChatbotController(ChatbotEngineService chatbotEngineService) {
        this.chatbotEngineService = chatbotEngineService;
    }

    @GetMapping("/services")
    public ResponseEntity<List<ServiceConsultingSummaryDto>> listServices() {
        return ResponseEntity.ok(chatbotEngineService.getConsultingServices());
    }

    @PostMapping("/ask")
    public ResponseEntity<ChatbotResponseDto> ask(@RequestBody ChatbotRequestDto request) {
        ChatbotResponseDto response = chatbotEngineService.processQuestion(request);
        return ResponseEntity.ok(response);
    }
}
