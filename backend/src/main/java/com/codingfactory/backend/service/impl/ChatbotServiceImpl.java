package com.codingfactory.backend.service.impl;

import com.codingfactory.backend.dto.ChatbotRequestDto;
import com.codingfactory.backend.dto.ChatbotResponseDto;
import com.codingfactory.backend.dto.ServiceConsultingSummaryDto;
import com.codingfactory.backend.entity.Consultant;
import com.codingfactory.backend.entity.QuestionUtilisateur;
import com.codingfactory.backend.entity.ServiceConsulting;
import com.codingfactory.backend.repository.ConsultantRepository;
import com.codingfactory.backend.repository.QuestionUtilisateurRepository;
import com.codingfactory.backend.repository.ServiceConsultingRepository;
import com.codingfactory.backend.repository.SujetPfeRepository;
import com.codingfactory.backend.service.ChatbotService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class ChatbotServiceImpl implements ChatbotService {

    private static final String DEFAULT_SERVICE = "Service de consulting général";
    private static final String DEFAULT_CONSULTANT = "Équipe CodingFactory";

    private static final Map<String, String> INTENTION_LABELS = Map.of(
            "DEVELOPPEMENT_WEB", "Développement web & applications",
            "CYBERSECURITE", "Cybersécurité & audit",
            "FORMATION", "Formation informatique",
            "CONSEIL", "Conseil stratégique",
            "PFE", "Projets de fin d'études",
            "CONTACT", "Contact & informations",
            "SALUTATION", "Accueil",
            "GENERAL", "Demande générale"
    );

    private final ServiceConsultingRepository serviceConsultingRepository;
    private final ConsultantRepository consultantRepository;
    private final QuestionUtilisateurRepository questionUtilisateurRepository;
    private final SujetPfeRepository sujetPfeRepository;

    public ChatbotServiceImpl(ServiceConsultingRepository serviceConsultingRepository,
                             ConsultantRepository consultantRepository,
                             QuestionUtilisateurRepository questionUtilisateurRepository,
                             SujetPfeRepository sujetPfeRepository) {
        this.serviceConsultingRepository = serviceConsultingRepository;
        this.consultantRepository = consultantRepository;
        this.questionUtilisateurRepository = questionUtilisateurRepository;
        this.sujetPfeRepository = sujetPfeRepository;
    }

    @Override
    public List<ServiceConsultingSummaryDto> listServices() {
        return serviceConsultingRepository.findAll().stream()
                .sorted(Comparator.comparing(ServiceConsulting::getNom))
                .map(service -> new ServiceConsultingSummaryDto(
                        service.getId(),
                        service.getNom(),
                        service.getDescription(),
                        consultantRepository.findByServiceConsultingId(service.getId()).size()))
                .toList();
    }

    @Override
    public ChatbotResponseDto processQuestion(ChatbotRequestDto request) {
        String question = request.getQuestion();
        String lowerQuestion = normalize(question);
        String prenom = normalizePrenom(request.getPrenom());

        Classification classification = classify(lowerQuestion);
        if ("GENERAL".equals(classification.intention())) {
            classification = enrichGeneralFromCatalog(lowerQuestion).orElse(classification);
        }

        String intentionLabel = INTENTION_LABELS.getOrDefault(classification.intention(), classification.intention());
        String response = buildResponse(classification, prenom);

        Optional<ServiceConsulting> serviceOpt = resolveService(classification);
        ServiceConsulting serviceEntity = serviceOpt.orElse(null);

        Consultant consultantEntity = null;
        String consultantLabel = DEFAULT_CONSULTANT;
        String consultantEmail = null;
        String serviceLabel = classification.serviceName();
        String serviceDescription = null;

        if (serviceEntity != null) {
            serviceLabel = serviceEntity.getNom();
            serviceDescription = serviceEntity.getDescription();
            List<Consultant> consultants = consultantRepository.findByServiceConsultingId(serviceEntity.getId());
            consultantEntity = pickConsultant(consultants, classification.intention()).orElse(null);
            if (consultantEntity != null) {
                consultantLabel = consultantEntity.getPrenom() + " " + consultantEntity.getNom()
                        + " — " + consultantEntity.getSpecialite();
                consultantEmail = consultantEntity.getEmail();
                if (!"SALUTATION".equals(classification.intention()) && !"PFE".equals(classification.intention())
                        && !"CONTACT".equals(classification.intention())) {
                    response = response + " Notre référent pour ce domaine est " + consultantEntity.getPrenom()
                            + " (" + consultantEntity.getEmail() + ").";
                }
            } else if (!consultants.isEmpty()) {
                consultantEntity = consultants.get(0);
                consultantLabel = consultantEntity.getPrenom() + " " + consultantEntity.getNom();
                consultantEmail = consultantEntity.getEmail();
            }
        } else if ("GENERAL".equals(classification.intention()) || "SALUTATION".equals(classification.intention())
                || "PFE".equals(classification.intention()) || "CONTACT".equals(classification.intention())) {
            serviceLabel = classification.serviceName();
        }

        QuestionUtilisateur questionUtilisateur = new QuestionUtilisateur();
        questionUtilisateur.setContenu(question);
        questionUtilisateur.setIntention(classification.intention());
        questionUtilisateur.setServiceConsulting(serviceEntity);
        questionUtilisateur.setConsultant(consultantEntity);
        questionUtilisateurRepository.save(questionUtilisateur);

        return new ChatbotResponseDto(
                classification.intention(),
                intentionLabel,
                response,
                serviceLabel,
                serviceDescription,
                consultantLabel,
                consultantEmail
        );
    }

    private String buildResponse(Classification classification, String prenom) {
        if ("PFE".equals(classification.intention())) {
            long actifs = sujetPfeRepository.countByActifTrue();
            String base = "CodingFactory propose actuellement " + actifs + " sujet(s) PFE actif(s). "
                    + "Consultez la section PFE pour la liste, les projets réalisés et le formulaire de candidature.";
            return personalize(base, prenom);
        }
        return personalize(classification.response(), prenom);
    }

    private Optional<Classification> enrichGeneralFromCatalog(String lowerQuestion) {
        if (lowerQuestion.length() < 4) {
            return Optional.empty();
        }
        return serviceConsultingRepository.findAll().stream()
                .map(service -> new ScoredService(service, scoreServiceMatch(lowerQuestion, service)))
                .filter(s -> s.score() > 0)
                .max(Comparator.comparingInt(ScoredService::score))
                .map(s -> mapServiceToClassification(s.service()));
    }

    private static int scoreServiceMatch(String question, ServiceConsulting service) {
        int score = 0;
        score += tokenOverlap(question, normalize(service.getNom())) * 3;
        score += tokenOverlap(question, normalize(service.getDescription()));
        return score;
    }

    private static int tokenOverlap(String question, String haystack) {
        int score = 0;
        for (String token : question.split("\\s+")) {
            if (token.length() >= 4 && haystack.contains(token)) {
                score++;
            }
        }
        return score;
    }

    private Classification mapServiceToClassification(ServiceConsulting service) {
        String nom = normalize(service.getNom());
        if (nom.contains("developpement") || nom.contains("logiciel")) {
            return new Classification("DEVELOPPEMENT_WEB", service.getNom(),
                    "D'après votre message, le service « " + service.getNom() + " » semble correspondre à votre besoin.");
        }
        if (nom.contains("cyber") || nom.contains("securite")) {
            return new Classification("CYBERSECURITE", service.getNom(),
                    "Votre demande semble liée à « " + service.getNom() + " ».");
        }
        if (nom.contains("formation")) {
            return new Classification("FORMATION", service.getNom(),
                    "Nous vous orientons vers « " + service.getNom() + " ».");
        }
        if (nom.contains("conseil")) {
            return new Classification("CONSEIL", service.getNom(),
                    "Le pôle « " + service.getNom() + " » peut vous accompagner.");
        }
        return new Classification("GENERAL", service.getNom(),
                "Voici le service le plus proche de votre demande : " + service.getNom() + ".");
    }

    private Optional<ServiceConsulting> resolveService(Classification classification) {
        if ("GENERAL".equals(classification.intention()) || "SALUTATION".equals(classification.intention())
                || "PFE".equals(classification.intention()) || "CONTACT".equals(classification.intention())) {
            return Optional.empty();
        }
        Optional<ServiceConsulting> exact = serviceConsultingRepository.findByNomIgnoreCase(classification.serviceName());
        if (exact.isPresent()) {
            return exact;
        }
        String needle = normalize(classification.serviceName());
        return serviceConsultingRepository.findAll().stream()
                .filter(s -> normalize(s.getNom()).contains(needle) || needle.contains(normalize(s.getNom())))
                .findFirst();
    }

    private Optional<Consultant> pickConsultant(List<Consultant> consultants, String intention) {
        if (consultants.isEmpty()) {
            return Optional.empty();
        }
        if (consultants.size() == 1) {
            return Optional.of(consultants.get(0));
        }
        String[] hints = switch (intention) {
            case "CYBERSECURITE" -> new String[] {"audit", "secur", "pentest", "cyber"};
            case "FORMATION" -> new String[] {"form", "java", "spring", "angular"};
            case "CONSEIL" -> new String[] {"conseil", "strat", "transformation"};
            default -> new String[] {"dev", "web", "mobile", "logiciel"};
        };
        return consultants.stream()
                .max(Comparator.comparingInt(c -> scoreSpecialite(c.getSpecialite(), hints)));
    }

    private static int scoreSpecialite(String specialite, String[] hints) {
        if (!StringUtils.hasText(specialite)) {
            return 0;
        }
        String norm = normalize(specialite);
        int score = 0;
        for (String hint : hints) {
            if (norm.contains(hint)) {
                score++;
            }
        }
        return score;
    }

    private static String normalize(String text) {
        if (text == null) {
            return "";
        }
        return text.toLowerCase(Locale.ROOT)
                .replace('é', 'e')
                .replace('è', 'e')
                .replace('ê', 'e')
                .replace('à', 'a')
                .replace('ù', 'u')
                .replace('ç', 'c');
    }

    private static String normalizePrenom(String prenom) {
        if (!StringUtils.hasText(prenom)) {
            return null;
        }
        String trimmed = prenom.trim();
        if (trimmed.length() > 50) {
            trimmed = trimmed.substring(0, 50);
        }
        return trimmed.substring(0, 1).toUpperCase(Locale.ROOT) + trimmed.substring(1).toLowerCase(Locale.ROOT);
    }

    private static String personalize(String baseResponse, String prenom) {
        if (prenom == null) {
            return baseResponse;
        }
        return "Bonjour " + prenom + ", " + Character.toLowerCase(baseResponse.charAt(0)) + baseResponse.substring(1);
    }

    private static Classification classify(String lowerQuestion) {
        if (isGreeting(lowerQuestion)) {
            return new Classification(
                    "SALUTATION",
                    DEFAULT_SERVICE,
                    "Bienvenue chez CodingFactory. Je suis CodingBot : décrivez votre besoin (développement, cybersécurité, formation, conseil ou PFE) et je vous orienterai.");
        }
        if (containsAny(lowerQuestion, "pfe", "projet de fin", "stage", "sujet de pfe", "fin d'etude", "fin d etude")) {
            return new Classification(
                    "PFE",
                    "Module PFE CodingFactory",
                    "Placeholder");
        }
        if (containsAny(lowerQuestion, "contact", "email", "telephone", "horaire", "adresse", "ou etes", "localisation")) {
            return new Classification(
                    "CONTACT",
                    "CodingFactory — Contact",
                    "CodingFactory est joignable à contact@codingfactory.tn — Lun–Ven 9h–18h. Pour un sujet PFE ou une demande consulting, utilisez les modules dédiés de la plateforme.");
        }
        if (containsAny(lowerQuestion, "codingfactory", "coding factory", "qui etes", "presentation", "services")) {
            return new Classification(
                    "CONTACT",
                    "À propos de CodingFactory",
                    "CodingFactory accompagne les entreprises en développement logiciel, cybersécurité, formation et conseil IT. Posez une question précise pour être orienté vers le bon expert.");
        }
        if (containsAny(lowerQuestion, "site", "application", "web", "developpement", "mobile", "api", "logiciel")) {
            return new Classification(
                    "DEVELOPPEMENT_WEB",
                    "Développement logiciel",
                    "Votre besoin correspond au service de développement logiciel. Nous pouvons concevoir un site, une application ou une plateforme sur mesure.");
        }
        if (containsAny(lowerQuestion, "cyber", "securite", "audit", "vulnerabilite", "pentest", "firewall", "hack")) {
            return new Classification(
                    "CYBERSECURITE",
                    "Cyber sécurité",
                    "Votre demande relève de la cybersécurité. Nous réalisons des audits, sécurisons les infrastructures et partageons les bonnes pratiques.");
        }
        if (containsAny(lowerQuestion, "formation", "java", "spring", "angular", "cours", "apprendre")) {
            return new Classification(
                    "FORMATION",
                    "Formation informatique",
                    "Votre demande concerne la formation informatique. Nous proposons des parcours Java, Spring Boot, Angular et DevOps.");
        }
        if (containsAny(lowerQuestion, "consulting", "conseil", "strat", "strategie", "accompagnement")) {
            return new Classification(
                    "CONSEIL",
                    "Conseil stratégique",
                    "Votre besoin correspond au conseil stratégique. Nous vous aidons à choisir la bonne feuille de route pour votre projet.");
        }
        return new Classification(
                "GENERAL",
                DEFAULT_SERVICE,
                "Je peux vous orienter vers nos pôles développement, cybersécurité, formation ou conseil. Précisez votre besoin en quelques mots, ou demandez des informations sur les sujets PFE.");
    }

    private static boolean isGreeting(String text) {
        String t = text.trim();
        if (t.length() > 40) {
            return false;
        }
        return containsAny(t, "bonjour", "salut", "hello", "bonsoir", "coucou", "hey");
    }

    private static boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private record Classification(String intention, String serviceName, String response) {}

    private record ScoredService(ServiceConsulting service, int score) {}
}
