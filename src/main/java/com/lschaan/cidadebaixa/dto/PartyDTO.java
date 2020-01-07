package com.lschaan.cidadebaixa.dto;

import com.lschaan.cidadebaixa.type.ClubEnum;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyDTO {
    private String partyName;
    private LocalDate date;
    private Boolean openBar;
    private Double priceOnSite;
    private Double priceInAdvance;
    private String message;
    private ClubEnum club;
}
