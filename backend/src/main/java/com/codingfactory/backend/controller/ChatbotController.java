package com.codingfactory.backend.controller;

import com.codingfactory.backend.dto.ChatbotRequestDto;
import com.codingfactory.backend.dto.ChatbotResponseDto;
import com.codingfactory.backend.dto.ServiceConsultingSummaryDto;
import com.codingfactory.backend.service.ChatbotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/ask")
    public ResponseEntity<ChatbotResponseDto> ask(@Valid @RequestBody ChatbotRequestDto request) {
        return ResponseEntity.ok(chatbotService.processQuestion(request));
    }

    @GetMapping("/services")
    public ResponseEntity<List<ServiceConsultingSummaryDto>> listServices() {
        return ResponseEntity.ok(chatbotService.listServices());
    }
}
