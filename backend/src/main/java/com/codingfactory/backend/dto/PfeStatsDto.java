package com.codingfactory.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PfeStatsDto {
    private long sujetsTotal;
    private long sujetsActifs;
    private long projetsRealises;
    private long candidaturesEnAttente;
}
