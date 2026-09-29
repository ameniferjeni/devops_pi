package com.codingfactory.chatbot.dto;

public class ChatbotRequestDto {
    private String question;
    private String prenom;
    public ChatbotRequestDto() {}
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
}
