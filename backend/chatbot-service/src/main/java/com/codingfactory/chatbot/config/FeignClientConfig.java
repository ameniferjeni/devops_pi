package com.codingfactory.chatbot.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            var request = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
            if (request instanceof org.springframework.web.context.request.ServletRequestAttributes) {
                HttpServletRequest servletRequest = ((org.springframework.web.context.request.ServletRequestAttributes) request).getRequest();
                String auth = servletRequest.getHeader("Authorization");
                if (auth != null) {
                    requestTemplate.header("Authorization", auth);
                }
            }
        };
    }
}
