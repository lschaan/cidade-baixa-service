package com.lschaan.cidadebaixa.dto;

import com.lschaan.cidadebaixa.type.TicketEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDTO {
  private Double price;
  private TicketEnum type;
  private LocalDate dueDate;
}
