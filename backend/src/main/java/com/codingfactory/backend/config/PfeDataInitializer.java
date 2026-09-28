package com.codingfactory.backend.config;

import com.codingfactory.backend.entity.ProjetRealise;
import com.codingfactory.backend.entity.SujetPfe;
import com.codingfactory.backend.entity.Utilisateur;
import com.codingfactory.backend.enums.Role;
import com.codingfactory.backend.repository.ProjetRealiseRepository;
import com.codingfactory.backend.repository.SujetPfeRepository;
import com.codingfactory.backend.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class PfeDataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final SujetPfeRepository sujetPfeRepository;
    private final ProjetRealiseRepository projetRealiseRepository;

    public PfeDataInitializer(UtilisateurRepository utilisateurRepository,
                              SujetPfeRepository sujetPfeRepository,
                              ProjetRealiseRepository projetRealiseRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.sujetPfeRepository = sujetPfeRepository;
        this.projetRealiseRepository = projetRealiseRepository;
    }

    @Override
    public void run(String... args) {
        seedCandidat("Dupont", "Karim", "karim.dupont@etudiant.tn");
        seedCandidat("Bouazizi", "Ines", "ines.bouazizi@etudiant.tn");
        seedCandidat("Jebali", "Mohamed", "mohamed.jebali@etudiant.tn");

        if (sujetPfeRepository.count() == 0) {
            seedSujet(
                    "Plateforme de gestion des PFE",
                    "Concevoir une application web permettant de publier des sujets PFE, gérer les candidatures et présenter les projets réalisés.",
                    "Génie logiciel",
                    "Spring Boot, Angular, MySQL",
                    "CodingFactory",
                    true);
            seedSujet(
                    "Chatbot intelligent de consulting",
                    "Développer un assistant capable d'analyser les demandes clients et d'orienter vers les services et consultants adaptés.",
                    "Intelligence artificielle",
                    "Java, NLP léger, Angular",
                    "CodingFactory",
                    true);
            seedSujet(
                    "Audit automatisé de sécurité web",
                    "Mettre en place un outil d'analyse des vulnérabilités OWASP pour les applications web internes.",
                    "Cybersécurité",
                    "Python, OWASP ZAP, Docker",
                    "CodingFactory",
                    true);
            seedSujet(
                    "Tableau de bord DevOps",
                    "Centraliser les métriques CI/CD (Jenkins, GitHub Actions) dans un dashboard temps réel.",
                    "DevOps",
                    "React, Prometheus, Grafana",
                    "CodingFactory",
                    false);
        }

        if (projetRealiseRepository.count() == 0) {
            seedProjet(
                    "Portail RH CodingFactory",
                    "Application de gestion des congés et des évaluations pour une PME tunisienne.",
                    "Scrum avec sprints de 2 semaines, analyse UML, développement incrémental et tests JUnit.",
                    "Livraison en production sur Docker ; réduction de 40 % du temps de traitement des demandes RH.");
            seedProjet(
                    "Système de réservation cloud",
                    "Plateforme de réservation de salles avec notifications en temps réel.",
                    "Approche Kanban, prototypage Figma, API REST Spring Boot et WebSocket pour les alertes.",
                    "99,5 % de disponibilité sur 3 mois ; intégration réussie avec l'Active Directory de l'entreprise.");
        }
    }

    private void seedCandidat(String nom, String prenom, String email) {
        if (utilisateurRepository.findByEmail(email).isPresent()) {
            return;
        }
        Utilisateur u = new Utilisateur();
        u.setNom(nom);
        u.setPrenom(prenom);
        u.setEmail(email);
        u.setPassword("demo123");
        u.setRole(Role.CANDIDAT);
        utilisateurRepository.save(u);
    }

    private void seedSujet(String titre, String description, String domaine, String technologie, String entreprise, boolean actif) {
        SujetPfe sujet = new SujetPfe();
        sujet.setTitre(titre);
        sujet.setDescription(description);
        sujet.setDomaine(domaine);
        sujet.setTechnologie(technologie);
        sujet.setEntreprise(entreprise);
        sujet.setActif(actif);
        sujetPfeRepository.save(sujet);
    }

    private void seedProjet(String titre, String description, String methode, String resultat) {
        ProjetRealise projet = new ProjetRealise();
        projet.setTitre(titre);
        projet.setDescription(description);
        projet.setMethode(methode);
        projet.setResultat(resultat);
        projetRealiseRepository.save(projet);
    }
}
