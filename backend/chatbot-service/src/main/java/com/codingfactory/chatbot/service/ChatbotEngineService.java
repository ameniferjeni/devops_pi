package com.codingfactory.chatbot.service;

import com.codingfactory.chatbot.dto.ChatbotRequestDto;
import com.codingfactory.chatbot.dto.ChatbotResponseDto;
import com.codingfactory.chatbot.dto.ServiceConsultingSummaryDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ChatbotEngineService {

    public List<ServiceConsultingSummaryDto> getConsultingServices() {
        return List.of(
                ServiceConsultingSummaryDto.builder()
                        .id(1L)
                        .nom("Développement Software & Cloud Native")
                        .description("Conception d'applications web, mobile, microservices Spring Boot & Angular sur-mesure avec architecture Cloud.")
                        .consultantsCount(5)
                        .expertNom("Sami Mansour")
                        .expertRole("Architecte Lead Software & Cloud")
                        .expertEmail("sami.mansour@codingfactory.tn")
                        .expertPhone("+216 20 111 222")
                        .technologies(List.of("Java / Spring Boot", "Angular / React", "Microservices", "Docker & Kubernetes", "AWS / Azure"))
                        .build(),
                ServiceConsultingSummaryDto.builder()
                        .id(2L)
                        .nom("Cybersécurité & Audit SI")
                        .description("Audits de sécurité automatisés et manuels, tests d'intrusion (Pentest) et mise en conformité ISO 27001 & RGPD.")
                        .consultantsCount(4)
                        .expertNom("Mehdi Gharbi")
                        .expertRole("Lead Expert Cybersécurité & Pentest")
                        .expertEmail("mehdi.gharbi@codingfactory.tn")
                        .expertPhone("+216 20 333 444")
                        .technologies(List.of("Pentest Web & API", "Audit Code Source", "Conformité RGPD / ISO 27001", "SOC & Monitoring"))
                        .build(),
                ServiceConsultingSummaryDto.builder()
                        .id(3L)
                        .nom("Formation Informatique & Coaching")
                        .description("Bootcamps et formations certifiantes sur-mesure pour vos équipes (Java, Spring Boot, Angular, DevOps & AI).")
                        .consultantsCount(6)
                        .expertNom("Amina Triki")
                        .expertRole("Directrice Formations & Tech Coach")
                        .expertEmail("amina.triki@codingfactory.tn")
                        .expertPhone("+216 20 555 666")
                        .technologies(List.of("Java 21 / Spring Boot 3", "Angular 18", "DevOps & CI/CD", "Prompt Engineering & IA"))
                        .build(),
                ServiceConsultingSummaryDto.builder()
                        .id(4L)
                        .nom("Conseil Stratégique IT & Gouvernance")
                        .description("Accompagnement de la direction SI dans la gouvernance, le schéma directeur et la transformation digitale.")
                        .consultantsCount(3)
                        .expertNom("Karim Trabelsi")
                        .expertRole("Senior Consultant IT Strategy & Gouvernance")
                        .expertEmail("karim.trabelsi@codingfactory.tn")
                        .expertPhone("+216 20 777 888")
                        .technologies(List.of("Schéma Directeur SI", "Audit d'Architecture", "Agile Transformation", "Gouvernance IT"))
                        .build(),
                ServiceConsultingSummaryDto.builder()
                        .id(5L)
                        .nom("Intelligence Artificielle & Data Science")
                        .description("Intégration de modèles Machine Learning, LLM, Computer Vision et pipelines MLOps pour automatiser vos processus.")
                        .consultantsCount(4)
                        .expertNom("Dr. Yassine Ben Romdhane")
                        .expertRole("Lead Data Scientist & Expert IA")
                        .expertEmail("yassine.benromdhane@codingfactory.tn")
                        .expertPhone("+216 20 999 000")
                        .technologies(List.of("Python & PyTorch", "Generative AI / LLM", "MLOps & CI/CD ML", "Business Intelligence"))
                        .build(),
                ServiceConsultingSummaryDto.builder()
                        .id(6L)
                        .nom("Platform Engineering & DevSecOps Infrastructure")
                        .description("Conception d'Internal Developer Platforms (IDP), automatisation des pipelines DevSecOps et gestion des secrets Vault.")
                        .consultantsCount(4)
                        .expertNom("Inès Chebbi")
                        .expertRole("Lead Platform Engineer & DevSecOps Specialist")
                        .expertEmail("ines.chebbi@codingfactory.tn")
                        .expertPhone("+216 20 888 999")
                        .technologies(List.of("Kubernetes", "ArgoCD", "Terraform", "HashiCorp Vault", "Backstage IDP"))
                        .build(),
                ServiceConsultingSummaryDto.builder()
                        .id(7L)
                        .nom("Blockchain, Web3 & FinTech Security")
                        .description("Conception d'architectures distribuées, audit de Smart Contracts Solidity et sécurisation des protocoles Web3.")
                        .consultantsCount(3)
                        .expertNom("Tarek Ben Ammar")
                        .expertRole("Lead Architect Blockchain & Smart Contracts")
                        .expertEmail("tarek.benammar@codingfactory.tn")
                        .expertPhone("+216 20 777 999")
                        .technologies(List.of("Solidity", "Ethereum", "Hyperledger Fabric", "Web3.js", "Zero-Knowledge Proofs"))
                        .build(),
                ServiceConsultingSummaryDto.builder()
                        .id(8L)
                        .nom("PFE & Partenariats Académiques")
                        .description("Incubation et encadrement de projets de fin d'études informatiques à fort impact technologique.")
                        .consultantsCount(5)
                        .expertNom("Youssef Ben Ali")
                        .expertRole("Directeur PFE & Relations Académiques")
                        .expertEmail("pfe@codingfactory.tn")
                        .expertPhone("+216 20 123 456")
                        .technologies(List.of("Encadrement Technique", "Sujets PFE Inédits", "Coaching Carrière", "Recrutement"))
                        .build()
        );
    }

    public ChatbotResponseDto processQuestion(ChatbotRequestDto request) {
        String rawQuestion = request.getQuestion() == null ? "" : request.getQuestion();
        String q = rawQuestion.toLowerCase(Locale.ROOT).trim();

        String prenom = (request.getPrenom() != null && !request.getPrenom().trim().isEmpty())
                ? request.getPrenom().trim()
                : "Cher visiteur";

        String entrepriseInfo = (request.getEntreprise() != null && !request.getEntreprise().trim().isEmpty())
                ? " (" + request.getEntreprise().trim() + ")"
                : "";

        // Default General Intent
        String intention = "GENERAL";
        String intentionLabel = "Questions générales";
        String serviceNom = "Services de Consulting CodingFactory";
        String serviceDesc = "CodingFactory offre une expertise de pointe en ingénierie logicielle, cybersécurité, conseil IT, platform engineering, blockchain, data/IA et formation.";
        String consultantNom = "Équipe CodingFactory Consulting";
        String consultantRole = "Experts Consulting & Support";
        String consultantEmail = "contact@codingfactory.tn";
        String consultantPhone = "+216 71 000 000";
        String consultantAvatar = "🏢";
        String actionType = "INFO";

        List<String> suggestions = new ArrayList<>(List.of(
                "Quels sont vos services de consulting ?",
                "Platform Engineering & DevSecOps",
                "Blockchain & Smart Contracts Web3",
                "J'ai besoin d'un audit cybersécurité",
                "Développement d'une application web/mobile",
                "Quels sont vos tarifs / TJM ?"
        ));

        StringBuilder response = new StringBuilder();

        // Salutations / Greetings
        if (q.matches(".*(bonjour|salut|coucou|hello|bonsoir|hey|hi|présente|presente).*") && q.length() < 30) {
            intention = "SALUTATION";
            intentionLabel = "Accueil & Présentation";
            response.append("Bonjour ").append(prenom).append(entrepriseInfo).append(" ! 👋\n\n")
                    .append("Je suis **CodingBot**, votre assistant virtuel dédié aux services de consulting et à l'orientation technique chez **CodingFactory**.\n\n")
                    .append("Comment puis-je vous aider aujourd'hui ? Vous pouvez me poser une question générale, demander un devis, ou rechercher un expert par domaine.");
            suggestions = List.of(
                    "Présenter les services de consulting",
                    "Platform Engineering & DevSecOps",
                    "Blockchain & Web3 Security",
                    "Développement Software & Cloud",
                    "Audit Cybersécurité & Pentest"
            );
        }
        // Platform Engineering / DevSecOps
        else if (q.contains("platform") || q.contains("devsecops") || q.contains("argocd") || q.contains("vault") || q.contains("backstage") || q.contains("idp") || q.contains("internal developer")) {
            intention = "PLATFORM_ENGINEERING";
            intentionLabel = "Platform Engineering & DevSecOps";
            serviceNom = "Platform Engineering & DevSecOps Infrastructure";
            serviceDesc = "Industrialisez la livraison logicielle grâce aux plateformes internes pour développeurs (IDP), au GitOps (ArgoCD) et à la gestion centralisée des secrets.";
            consultantNom = "Inès Chebbi";
            consultantRole = "Lead Platform Engineer & DevSecOps Specialist";
            consultantEmail = "ines.chebbi@codingfactory.tn";
            consultantPhone = "+216 20 888 999";
            consultantAvatar = "🚀";
            actionType = "CONTACT_CONSULTANT";

            response.append("Excellente question ").append(prenom).append(" ! Le Platform Engineering révolutionne le quotidien des équipes de développement.\n\n")
                    .append("Notre pôle **Platform Engineering & DevSecOps** assure :\n")
                    .append("• **Mise en place d'Internal Developer Platforms (IDP avec Backstage)**\n")
                    .append("• **Déploiements GitOps continus avec ArgoCD & Kubernetes**\n")
                    .append("• **Sécurisation des pipelines CI/CD & Secrets avec HashiCorp Vault**\n\n")
                    .append("Notre experte référente est **Inès Chebbi** (Lead Platform Engineer). Vous pouvez programmer un échange technique dès maintenant.");
            suggestions = List.of(
                    "Prendre RDV avec Inès Chebbi",
                    "Auditer notre architecture GitOps",
                    "Découvrir la Blockchain & Web3",
                    "Voir d'autres services"
            );
        }
        // Blockchain & Web3
        else if (q.contains("blockchain") || q.contains("web3") || q.contains("solidity") || q.contains("smart contract") || q.contains("ethereum") || q.contains("crypto") || q.contains("fintech") || q.contains("nft")) {
            intention = "BLOCKCHAIN_WEB3";
            intentionLabel = "Blockchain, Web3 & FinTech";
            serviceNom = "Blockchain, Web3 & FinTech Security";
            serviceDesc = "Développement de protocoles décentralisés, audit de sécurité des Smart Contracts et architectures distribuées pour la FinTech.";
            consultantNom = "Tarek Ben Ammar";
            consultantRole = "Lead Architect Blockchain & Smart Contracts";
            consultantEmail = "tarek.benammar@codingfactory.tn";
            consultantPhone = "+216 20 777 999";
            consultantAvatar = "🔗";
            actionType = "CONTACT_CONSULTANT";

            response.append("Sujet passionnant ").append(prenom).append(" ! La technologie Blockchain et le Web3 ouvrent d'immenses opportunités FinTech.\n\n")
                    .append("Notre pôle **Blockchain & Web3** propose :\n")
                    .append("• **Audit de sécurité & Vérification formelle de Smart Contracts (Solidity)**\n")
                    .append("• **Développement de dApps & Architectures Ethereum / Hyperledger Fabric**\n")
                    .append("• **Protocoles de confidentialité Zero-Knowledge Proofs (ZKP)**\n\n")
                    .append("Notre expert référent pour ce domaine est **Tarek Ben Ammar** (Lead Architect Blockchain).");
            suggestions = List.of(
                    "Prendre RDV avec Tarek Ben Ammar",
                    "Auditer un Smart Contract",
                    "Platform Engineering",
                    "Voir d'autres services"
            );
        }
        // Cybersécurité
        else if (q.contains("cyber") || q.contains("securit") || q.contains("audit") || q.contains("pentest") || q.contains("vulnerabilite") || q.contains("iso27001") || q.contains("rgpd") || q.contains("hack")) {
            intention = "CYBERSECURITE";
            intentionLabel = "Cybersécurité & Audit SI";
            serviceNom = "Cybersécurité & Audit SI";
            serviceDesc = "Protégez vos applications et infrastructures grâce à nos audits approfondis, tests d'intrusion (Pentest) et conformité RGPD / ISO 27001.";
            consultantNom = "Mehdi Gharbi";
            consultantRole = "Lead Expert Cybersécurité & Pentester Certifié";
            consultantEmail = "mehdi.gharbi@codingfactory.tn";
            consultantPhone = "+216 20 333 444";
            consultantAvatar = "🛡️";
            actionType = "CONTACT_CONSULTANT";

            response.append("Excellente initiative ").append(prenom).append(" ! La sécurité de vos données est une priorité.\n\n")
                    .append("Notre pôle **Cybersécurité & Audit SI** propose :\n")
                    .append("• **Tests d'intrusion (Pentest)** Web, Mobile et Infrastructure\n")
                    .append("• **Audit de code source** & Détection de vulnérabilités OWASP\n")
                    .append("• **Accompagnement ISO 27001 & RGPD**\n\n")
                    .append("Notre consultant référent pour ce domaine est **Mehdi Gharbi** (Lead Cybersécurité). Vous pouvez planifier une session de cadrage dès maintenant.");
            suggestions = List.of(
                    "Prendre RDV avec Mehdi Gharbi",
                    "Demander un devis Pentest",
                    "Quelles sont les étapes d'un audit ?",
                    "Voir d'autres services"
            );
        }
        // Développement Web & Cloud
        else if (q.contains("web") || q.contains("dev") || q.contains("app") || q.contains("mobile") || q.contains("cloud") || q.contains("spring") || q.contains("angular") || q.contains("react") || q.contains("microservice") || q.contains("logiciel") || q.contains("architecture")) {
            intention = "DEVELOPPEMENT_SOFTWARE";
            intentionLabel = "Développement Software & Cloud Native";
            serviceNom = "Développement Software & Cloud Native";
            serviceDesc = "Conception d'applications sur-mesure, scalables et haute performance basées sur les meilleures pratiques d'architecture Cloud.";
            consultantNom = "Sami Mansour";
            consultantRole = "Architecte Lead Software & Cloud Native";
            consultantEmail = "sami.mansour@codingfactory.tn";
            consultantPhone = "+216 20 111 222";
            consultantAvatar = "💻";
            actionType = "CONTACT_CONSULTANT";

            response.append("Très bien ").append(prenom).append(" ! Nous sommes experts en développement logiciel sur-mesure.\n\n")
                    .append("Notre pôle **Développement & Cloud** intervient sur :\n")
                    .append("• **Applications Web & Mobile** (Angular 18, React, Flutter, Spring Boot 3)\n")
                    .append("• **Architecture Microservices & APIs REST**\n")
                    .append("• **Deploiement Cloud Native** (AWS, Azure, Docker, Kubernetes)\n\n")
                    .append("Notre expert référent est **Sami Mansour** (Architecte Lead Software). Il peut analyser votre besoin technique.");
            suggestions = List.of(
                    "Prendre RDV avec Sami Mansour",
                    "Demander un chiffrage de projet",
                    "Technologies maîtrisées",
                    "Voir nos offres de formation"
            );
        }
        // Formations
        else if (q.contains("formation") || q.contains("cours") || q.contains("bootcamp") || q.contains("coaching") || q.contains("certification") || q.contains("java") || q.contains("apprendre")) {
            intention = "FORMATION";
            intentionLabel = "Formation Informatique & Coaching";
            serviceNom = "Formation Informatique & Coaching Technique";
            serviceDesc = "Montez en compétences grâce à nos formations certifiantes et bootcamps intensifs dispensés par des professionnels du secteur.";
            consultantNom = "Amina Triki";
            consultantRole = "Directrice des Formations & Tech Coach";
            consultantEmail = "amina.triki@codingfactory.tn";
            consultantPhone = "+216 20 555 666";
            consultantAvatar = "🎓";
            actionType = "CONTACT_CONSULTANT";

            response.append("Bonne idée ").append(prenom).append(" ! Le développement des compétences est la clé de la réussite IT.\n\n")
                    .append("Notre pôle **Formation & Coaching** propose :\n")
                    .append("• **Formations Java 21 & Spring Boot 3** (Intensif & Avancé)\n")
                    .append("• **Formations Frontend Modern** (Angular 18 & TypeScript)\n")
                    .append("• **DevOps, Docker, Kubernetes & CI/CD**\n")
                    .append("• **Bootcamps personnalisés pour entreprises**\n\n")
                    .append("Notre responsable des formations est **Amina Triki**. Elle saura adapter le programme à vos objectifs.");
            suggestions = List.of(
                    "Prendre RDV avec Amina Triki",
                    "Demander le programme détaillé",
                    "Formations pour entreprises",
                    "Voir les services consulting"
            );
        }
        // Conseil Stratégique
        else if (q.contains("conseil") || q.contains("strategie") || q.contains("gouvernance") || q.contains("transformation") || q.contains("schema") || q.contains("consulting")) {
            intention = "CONSEIL_STRATEGIQUE";
            intentionLabel = "Conseil Stratégique IT & Gouvernance";
            serviceNom = "Conseil Stratégique IT & Gouvernance";
            serviceDesc = "Pilotage de la transformation digitale, urbanisation du SI et gouvernance des systèmes d'information.";
            consultantNom = "Karim Trabelsi";
            consultantRole = "Senior Consultant IT Strategy";
            consultantEmail = "karim.trabelsi@codingfactory.tn";
            consultantPhone = "+216 20 777 888";
            consultantAvatar = "📊";
            actionType = "CONTACT_CONSULTANT";

            response.append("Absolument ").append(prenom).append(". Un alignement stratégique du SI garantit le succès d'entreprise.\n\n")
                    .append("Notre pôle **Conseil Stratégique IT** vous accompagne sur :\n")
                    .append("• **Élaboration de Schémas Directeurs SI**\n")
                    .append("• **Audit d'Architecture & Rationalisation Legacy**\n")
                    .append("• **Accompagnement à la Transformation Digitale & Agile**\n\n")
                    .append("Notre consultant senior référent est **Karim Trabelsi**. Il est à votre disposition pour un échange stratégique.");
            suggestions = List.of(
                    "Prendre RDV avec Karim Trabelsi",
                    "Auditer notre architecture SI",
                    "Modalités d'accompagnement",
                    "Voir nos tarifs"
            );
        }
        // IA & Data Science
        else if (q.contains("ia") || q.contains("ai") || q.contains("data") || q.contains("machine learning") || q.contains("deep learning") || q.contains("python") || q.contains("mlops") || q.contains("intelligence artificielle") || q.contains("llm") || q.contains("gpt")) {
            intention = "AI_DATA";
            intentionLabel = "Intelligence Artificielle & Data Science";
            serviceNom = "Intelligence Artificielle & Data Science";
            serviceDesc = "Valorisez vos données grâce à l'IA générative, au Machine Learning et à la mise en place de pipelines MLOps sécurisés.";
            consultantNom = "Dr. Yassine Ben Romdhane";
            consultantRole = "Lead Data Scientist & Expert IA";
            consultantEmail = "yassine.benromdhane@codingfactory.tn";
            consultantPhone = "+216 20 999 000";
            consultantAvatar = "🤖";
            actionType = "CONTACT_CONSULTANT";

            response.append("Ravi d'aborder ce sujet ").append(prenom).append(" ! L'IA est un levier de croissance majeur.\n\n")
                    .append("Notre pôle **Data Science & IA** assure :\n")
                    .append("• **Développement de modèles ML & Computer Vision**\n")
                    .append("• **Intégration de LLM & Agents IA personnalisés**\n")
                    .append("• **Industrialisation MLOps (CI/CD pour l'IA)**\n\n")
                    .append("Notre expert référent est le **Dr. Yassine Ben Romdhane** (PhD Data Science & IA).");
            suggestions = List.of(
                    "Prendre RDV avec Dr. Yassine",
                    "Cadrage d'un projet IA",
                    "Audit Data & Infrastructure",
                    "Autres domaines de consulting"
            );
        }
        // Tarifs & Devis
        else if (q.contains("tarif") || q.contains("prix") || q.contains("devis") || q.contains("tjm") || q.contains("cout") || q.contains("combien") || q.contains("budget") || q.contains("factur")) {
            intention = "PRICING";
            intentionLabel = "Tarifs & Devis Consulting";
            serviceNom = "Offres Commerciales & Modèles de Consulting";
            serviceDesc = "Propositions financières sur-mesure adaptées à votre mode de fonctionnement : Régie, Forfait ou Centre de Développement Dédié.";
            consultantNom = "Karim Trabelsi";
            consultantRole = "Directeur Business & Offres Consulting";
            consultantEmail = "contact@codingfactory.tn";
            consultantPhone = "+216 71 000 000";
            consultantAvatar = "💶";
            actionType = "REQUEST_QUOTE";

            response.append("Merci de votre intérêt ").append(prenom).append(" ! Concernant nos tarifs et modalités de facturation :\n\n")
                    .append("1. **Mode Régie (TJM)** : Facturation à la journée selon la séniorité des consultants.\n")
                    .append("2. **Mode Forfait** : Engagement de livrables et de calendrier avec chiffrage global fixe.\n")
                    .append("3. **Centre de Développement Dédié** : Équipe dédiée à votre projet avec gouvernance partagée.\n\n")
                    .append("Demandez une estimation personnalisée sous 24h auprès de notre équipe commerciale.");
            suggestions = List.of(
                    "Demander un devis gratuit",
                    "Prendre RDV commercial",
                    "Découvrir nos services",
                    "Consulter nos références"
            );
        }
        // PFE & Stage
        else if (q.contains("pfe") || q.contains("stage") || q.contains("sujet") || q.contains("etudiant") || q.contains("candidat") || q.contains("recrutement")) {
            intention = "PFE";
            intentionLabel = "Projets de Fin d'Études (PFE)";
            serviceNom = "Module PFE & Encadrement Académique";
            serviceDesc = "Encadrement personnalisé des étudiants sur des sujets de recherche et développement à forte valeur ajoutée.";
            consultantNom = "Youssef Ben Ali";
            consultantRole = "Coordinateur PFE & Partenariats Académiques";
            consultantEmail = "pfe@codingfactory.tn";
            consultantPhone = "+216 20 123 456";
            consultantAvatar = "🎓";
            actionType = "VIEW_PFE";

            response.append("Ravi d'accueillir les futurs ingénieurs ").append(prenom).append(" ! 🎓\n\n")
                    .append("CodingFactory propose plusieurs **sujets de PFE innovants** pour l'année universitaire en cours :\n")
                    .append("• **Platform Engineering & Multi-cloud Automation**\n")
                    .append("• **Blockchain & Audits Smart Contracts**\n")
                    .append("• **IA & Generative Agents** pour la cybersécurité\n\n")
                    .append("Consultez directement la liste des sujets PFE et déposez votre candidature en ligne !");
            suggestions = List.of(
                    "Accéder au module PFE",
                    "Contacter Youssef Ben Ali",
                    "Exigences et critères de sélection",
                    "Services de consulting"
            );
        }
        // Fallback / General questions
        else {
            response.append("Merci pour votre question ").append(prenom).append(".\n\n")
                    .append("En tant qu'assistant consulting de **CodingFactory**, je peux vous orienter vers nos 8 pôles d'expertise et nos consultants référents :\n")
                    .append("1. **Platform Engineering & DevSecOps** (Ing. Inès Chebbi)\n")
                    .append("2. **Blockchain & Web3 Security** (Arch. Tarek Ben Ammar)\n")
                    .append("3. **Développement Software & Cloud Native** (Arch. Sami Mansour)\n")
                    .append("4. **Cybersécurité & Audit SI** (Expert Mehdi Gharbi)\n")
                    .append("5. **Formation Informatique & Coaching** (Directrice Amina Triki)\n")
                    .append("6. **Conseil Stratégique IT** (Consultant Senior Karim Trabelsi)\n")
                    .append("7. **IA & Data Science** (Dr. Yassine Ben Romdhane)\n")
                    .append("8. **Module PFE & Encadrement** (Youssef Ben Ali)\n\n")
                    .append("Sélectionnez l'un des choix ci-dessous ou précisez votre besoin.");
        }

        return ChatbotResponseDto.builder()
                .intention(intention)
                .intentionLabel(intentionLabel)
                .reponse(response.toString())
                .serviceNom(serviceNom)
                .serviceDescription(serviceDesc)
                .consultantNom(consultantNom)
                .consultantRole(consultantRole)
                .consultantEmail(consultantEmail)
                .consultantPhone(consultantPhone)
                .consultantAvatar(consultantAvatar)
                .suggestions(suggestions)
                .actionType(actionType)
                .build();
    }
}
