package com.codingfactory.backend.config;

import com.codingfactory.backend.entity.Consultant;
import com.codingfactory.backend.entity.ServiceConsulting;
import com.codingfactory.backend.repository.ConsultantRepository;
import com.codingfactory.backend.repository.ServiceConsultingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class ChatbotDataInitializer implements CommandLineRunner {

    private final ServiceConsultingRepository serviceConsultingRepository;
    private final ConsultantRepository consultantRepository;

    public ChatbotDataInitializer(ServiceConsultingRepository serviceConsultingRepository,
                                  ConsultantRepository consultantRepository) {
        this.serviceConsultingRepository = serviceConsultingRepository;
        this.consultantRepository = consultantRepository;
    }

    @Override
    public void run(String... args) {
        ensureService(
                "Développement logiciel",
                "Conception de sites web, applications métier et plateformes sur mesure (Java, Spring, Angular).",
                new ConsultantSeed("Ben Ali", "Amine", "amine.benali@codingfactory.tn", "Développement web & mobile"));
        ensureService(
                "Cyber sécurité",
                "Audit de sécurité, durcissement d'infrastructure et conseil en bonnes pratiques.",
                new ConsultantSeed("Trabelsi", "Sarra", "sarra.trabelsi@codingfactory.tn", "Audit & pentest"));
        ensureService(
                "Formation informatique",
                "Formations techniques : Java, Spring Boot, Angular, DevOps et cybersécurité.",
                new ConsultantSeed("Mansouri", "Youssef", "youssef.mansouri@codingfactory.tn", "Formateur Java/Spring"));
        ensureService(
                "Conseil stratégique",
                "Accompagnement stratégique et choix des solutions adaptées à votre organisation.",
                new ConsultantSeed("Gharbi", "Leila", "leila.gharbi@codingfactory.tn", "Conseil IT & transformation"));
    }

    private void ensureService(String nom, String description, ConsultantSeed seed) {
        ServiceConsulting service = serviceConsultingRepository.findByNomIgnoreCase(nom)
                .orElseGet(() -> {
                    ServiceConsulting s = new ServiceConsulting();
                    s.setNom(nom);
                    s.setDescription(description);
                    return serviceConsultingRepository.save(s);
                });

        if (consultantRepository.findByServiceConsultingId(service.getId()).isEmpty()) {
            saveConsultant(seed, service);
        }
    }

    private void saveConsultant(ConsultantSeed seed, ServiceConsulting service) {
        Consultant consultant = new Consultant();
        consultant.setNom(seed.nom());
        consultant.setPrenom(seed.prenom());
        consultant.setEmail(seed.email());
        consultant.setSpecialite(seed.specialite());
        consultant.setServiceConsulting(service);
        consultantRepository.save(consultant);
    }

    private record ConsultantSeed(String nom, String prenom, String email, String specialite) {}
}
