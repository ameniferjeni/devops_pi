package com.codingfactory.backend.service;

import com.codingfactory.backend.dto.ChatbotRequestDto;
import com.codingfactory.backend.dto.ChatbotResponseDto;
import com.codingfactory.backend.repository.ConsultantRepository;
import com.codingfactory.backend.repository.QuestionUtilisateurRepository;
import com.codingfactory.backend.repository.ServiceConsultingRepository;
import com.codingfactory.backend.repository.SujetPfeRepository;
import com.codingfactory.backend.service.impl.ChatbotServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChatbotServiceImplTest {

    @Mock
    private ServiceConsultingRepository serviceConsultingRepository;

    @Mock
    private ConsultantRepository consultantRepository;

    @Mock
    private QuestionUtilisateurRepository questionUtilisateurRepository;

    @Mock
    private SujetPfeRepository sujetPfeRepository;

    @InjectMocks
    private ChatbotServiceImpl chatbotService;

    @Test
    @DisplayName("Should process user question and return consulting response")
    void testProcessQuestion() {
        ChatbotRequestDto request = new ChatbotRequestDto();
        request.setQuestion("Comment sécuriser une architecture cloud ?");
        request.setPrenom("Salma");

        ChatbotResponseDto response = chatbotService.processQuestion(request);

        assertNotNull(response);
        assertNotNull(response.getReponse());
        assertNotNull(response.getService());
    }

    @Test
    @DisplayName("Should return available consulting services summary list")
    void testListServices() {
        var services = chatbotService.listServices();
        assertNotNull(services);
    }
}
