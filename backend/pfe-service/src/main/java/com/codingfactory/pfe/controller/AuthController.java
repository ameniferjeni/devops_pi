package com.codingfactory.pfe.controller;

import com.codingfactory.pfe.config.JwtUtils;
import com.codingfactory.pfe.dto.AuthRequestDto;
import com.codingfactory.pfe.dto.AuthResponseDto;
import com.codingfactory.pfe.entity.Utilisateur;
import com.codingfactory.pfe.enums.Role;
import com.codingfactory.pfe.repository.UtilisateurRepository;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping({"/api/auth", "/auth"})
@CrossOrigin(origins = "*")
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final JwtUtils jwtUtils;

    public AuthController(UtilisateurRepository utilisateurRepository, JwtUtils jwtUtils) {
        this.utilisateurRepository = utilisateurRepository;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto request) {
        String email = request.getEmail() == null ? "" : request.getEmail().trim().toLowerCase(Locale.ROOT);
        String password = request.getPassword() == null ? "" : request.getPassword().trim();

        if (email.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(AuthResponseDto.builder()
                    .message("L'email et le mot de passe sont obligatoires.")
                    .build());
        }

        initDemoAccountsIfNeeded();

        Utilisateur user = utilisateurRepository.findByEmail(email).orElse(null);

        if (user == null) {
            if ("admin@codingfactory.tn".equalsIgnoreCase(email)) {
                user = createDemoUser("Admin", "CodingFactory", "admin@codingfactory.tn", "admin123", Role.ADMIN);
            } else if ("candidat@codingfactory.tn".equalsIgnoreCase(email)) {
                user = createDemoUser("Salma", "Mansouri", "candidat@codingfactory.tn", "candidat123", Role.CANDIDAT);
            } else {
                user = createDemoUser("Utilisateur", "Demo", email, password, Role.CANDIDAT);
            }
        }

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name(), user.getPrenom(), user.getNom());

        return ResponseEntity.ok(AuthResponseDto.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .role(user.getRole().name())
                .message("Connexion réussie sous le rôle " + user.getRole().name())
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequestDto request) {
        String email = request.getEmail() == null ? "" : request.getEmail().trim().toLowerCase(Locale.ROOT);
        String password = request.getPassword() == null ? "" : request.getPassword().trim();
        String nom = request.getNom() == null ? "Candidat" : request.getNom().trim();
        String prenom = request.getPrenom() == null ? "Nouveau" : request.getPrenom().trim();
        String roleStr = request.getRole() == null ? "CANDIDAT" : request.getRole().trim().toUpperCase(Locale.ROOT);

        if (email.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(AuthResponseDto.builder()
                    .message("L'email et le mot de passe sont requis.")
                    .build());
        }

        Role role = "ADMIN".equalsIgnoreCase(roleStr) ? Role.ADMIN : Role.CANDIDAT;

        Utilisateur u = utilisateurRepository.findByEmail(email).orElseGet(() -> {
            Utilisateur newUser = new Utilisateur();
            newUser.setNom(nom);
            newUser.setPrenom(prenom);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setRole(role);
            return utilisateurRepository.save(newUser);
        });

        String token = jwtUtils.generateToken(u.getEmail(), u.getRole().name(), u.getPrenom(), u.getNom());

        return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponseDto.builder()
                .token(token)
                .id(u.getId())
                .email(u.getEmail())
                .nom(u.getNom())
                .prenom(u.getPrenom())
                .role(u.getRole().name())
                .message("Compte créé avec succès !")
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT manquant.");
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT invalide.");
        }

        Claims claims = jwtUtils.parseClaims(token);
        String email = claims.getSubject();
        String role = claims.get("role", String.class);
        String prenom = claims.get("prenom", String.class);
        String nom = claims.get("nom", String.class);

        return ResponseEntity.ok(AuthResponseDto.builder()
                .token(token)
                .email(email)
                .nom(nom)
                .prenom(prenom)
                .role(role)
                .message("Session JWT valide")
                .build());
    }

    private void initDemoAccountsIfNeeded() {
        if (utilisateurRepository.findByEmail("admin@codingfactory.tn").isEmpty()) {
            createDemoUser("Admin", "CodingFactory", "admin@codingfactory.tn", "admin123", Role.ADMIN);
        }
        if (utilisateurRepository.findByEmail("candidat@codingfactory.tn").isEmpty()) {
            createDemoUser("Salma", "Mansouri", "candidat@codingfactory.tn", "candidat123", Role.CANDIDAT);
        }
    }

    private Utilisateur createDemoUser(String prenom, String nom, String email, String password, Role role) {
        Utilisateur u = new Utilisateur();
        u.setNom(nom);
        u.setPrenom(prenom);
        u.setEmail(email);
        u.setPassword(password);
        u.setRole(role);
        return utilisateurRepository.save(u);
    }
}
