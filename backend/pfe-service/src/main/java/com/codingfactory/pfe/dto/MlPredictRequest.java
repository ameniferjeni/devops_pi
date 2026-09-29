package com.codingfactory.pfe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MlPredictRequest {

    @JsonProperty("candidature_id")
    private Long candidatureId;

    @JsonProperty("message_motivation")
    private String messageMotivation;

    @JsonProperty("domaine")
    private String domaine;

    @JsonProperty("technologie")
    private String technologie;

    @JsonProperty("entreprise")
    private String entreprise;

    @JsonProperty("sujet_actif")
    private boolean sujetActif;

    @JsonProperty("nb_candidatures_candidat")
    private long nbCandidaturesCandidat;

    @JsonProperty("nb_candidatures_sujet")
    private long nbCandidaturesSujet;
}
