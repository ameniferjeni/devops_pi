package com.codingfactory.pfe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MlPredictResponse {

    @JsonProperty("probabilite_acceptation")
    private Double probabiliteAcceptation;

    @JsonProperty("score_pourcentage")
    private Integer scorePourcentage;

    @JsonProperty("decision_suggeree")
    private String decisionSuggeree;

    @JsonProperty("explication")
    private String explication;
}