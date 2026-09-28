package com.codingfactory.backend.service;

import com.codingfactory.backend.dto.ChatbotRequestDto;
import com.codingfactory.backend.dto.ChatbotResponseDto;
import com.codingfactory.backend.dto.ServiceConsultingSummaryDto;

import java.util.List;

public interface ChatbotService {
    ChatbotResponseDto processQuestion(ChatbotRequestDto request);

    List<ServiceConsultingSummaryDto> listServices();
}
