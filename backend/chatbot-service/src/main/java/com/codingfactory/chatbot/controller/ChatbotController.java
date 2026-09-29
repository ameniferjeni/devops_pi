package com.codingfactory.chatbot.controller;

import com.codingfactory.chatbot.dto.ChatbotRequestDto;
import com.codingfactory.chatbot.dto.ChatbotResponseDto;
import com.codingfactory.chatbot.dto.ServiceConsultingSummaryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*")
public class ChatbotController {

    @GetMapping("/services")
    public ResponseEntity<List<ServiceConsultingSummaryDto>> listServices() {
        List<ServiceConsultingSummaryDto> services = List.of(
                new ServiceConsultingSummaryDto(1L, "Développement Software & Cloud", "Conception d'applications web, mobile et architectures cloud sur-mesure.", 5),
                new ServiceConsultingSummaryDto(2L, "Cybersécurité & Audit SI", "Audits de sécurité, tests d'intrusion (pentest) et conformité RGPD/ISO.", 4),
                new ServiceConsultingSummaryDto(3L, "Formation Informatique & Coaching", "Formations certifiantes Java, Spring Boot, Angular, DevOps et IA.", 6),
                new ServiceConsultingSummaryDto(4L, "Conseil Stratégique IT", "Accompagnement dans la transformation numérique et gouvernance SI.", 3)
        );
        return ResponseEntity.ok(services);
    }

    @PostMapping("/ask")
    public ResponseEntity<ChatbotResponseDto> ask(@RequestBody ChatbotRequestDto request) {
        String question = request.getQuestion() == null ? "" : request.getQuestion().toLowerCase(Locale.ROOT);
        String prenom = request.getPrenom() != null ? request.getPrenom() : "Cher visiteur";

        String intention = "GENERAL";
        String intentionLabel = "Demande générale";
        String serviceLabel = "Service de consulting général";
        String serviceDesc = "CodingFactory vous accompagne sur tous vos projets IT.";
        String consultantLabel = "Équipe CodingFactory";
        String consultantEmail = "contact@codingfactory.tn";
        String responseText = "Bonjour " + prenom + ", bienvenue chez CodingFactory ! Nous proposons des services de développement, cybersécurité, formation et conseil IT, ainsi que des sujets de PFE.";

        if (question.contains("pfe") || question.contains("stage") || question.contains("sujet")) {
            intention = "PFE";
            intentionLabel = "Projets de fin d'études";
            serviceLabel = "Module PFE CodingFactory";
            serviceDesc = "Accompagnement et encadrement des étudiants de fin d'études.";
            consultantLabel = "Coordinateur PFE — Youssef Ben Ali";
            consultantEmail = "pfe@codingfactory.tn";
            responseText = "CodingFactory propose actuellement plusieurs sujets PFE actifs. Consultez la section PFE pour déposer votre candidature !";
        } else if (question.contains("cyber") || question.contains("securit") || question.contains("audit") || question.contains("pentest")) {
            intention = "CYBERSECURITE";
            intentionLabel = "Cybersécurité & audit";
            serviceLabel = "Cybersécurité & Audit SI";
            serviceDesc = "Audits de sécurité, tests d'intrusion et sécurisation du SI.";
            consultantLabel = "Mehdi Gharbi — Expert Cybersécurité";
            consultantEmail = "mehdi.gharbi@codingfactory.tn";
            responseText = "Votre demande relève de la cybersécurité. Notre référent expert est Mehdi Gharbi (mehdi.gharbi@codingfactory.tn).";
        } else if (question.contains("web") || question.contains("dev") || question.contains("app") || question.contains("application") || question.contains("site")) {
            intention = "DEVELOPPEMENT_WEB";
            intentionLabel = "Développement web & applications";
            serviceLabel = "Développement Software & Cloud";
            serviceDesc = "Conception d'applications sur-mesure.";
            consultantLabel = "Sami Mansour — Architecte Fullstack";
            consultantEmail = "sami.mansour@codingfactory.tn";
            responseText = "Votre besoin concerne le développement logiciel. Notre référent est Sami Mansour (sami.mansour@codingfactory.tn).";
        } else if (question.contains("formation") || question.contains("cours") || question.contains("java") || question.contains("spring") || question.contains("angular")) {
            intention = "FORMATION";
            intentionLabel = "Formation informatique";
            serviceLabel = "Formation Informatique & Coaching";
            serviceDesc = "Formations certifiantes Java, Spring Boot, Angular et DevOps.";
            consultantLabel = "Amina Triki — Responsable Formations";
            consultantEmail = "amina.triki@codingfactory.tn";
            responseText = "Votre demande concerne nos formations informatiques. Notre responsable est Amina Triki (amina.triki@codingfactory.tn).";
        } else if (question.contains("conseil") || question.contains("strategie") || question.contains("consulting")) {
            intention = "CONSEIL";
            intentionLabel = "Conseil stratégique";
            serviceLabel = "Conseil Stratégique IT";
            serviceDesc = "Transformation numérique et gouvernance SI.";
            consultantLabel = "Karim Trabelsi — Senior Consultant IT";
            consultantEmail = "karim.trabelsi@codingfactory.tn";
            responseText = "Le pôle Conseil Stratégique IT peut vous accompagner. Contactez Karim Trabelsi (karim.trabelsi@codingfactory.tn).";
        }

        return ResponseEntity.ok(new ChatbotResponseDto(
                intention,
                intentionLabel,
                responseText,
                serviceLabel,
                serviceDesc,
                consultantLabel,
                consultantEmail
        ));
    }
}
