package com.lschaan.cidadebaixa.dto;

import com.lschaan.cidadebaixa.type.TicketEnum;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
