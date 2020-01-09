package com.lschaan.cidadebaixa.dto;

import com.lschaan.cidadebaixa.type.ClubEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyDTO {
  private String partyName;
  private LocalDate date;
  private Boolean openBar;
  private List<TicketDTO> tickets;
  private String message;
  private ClubEnum club;
}
