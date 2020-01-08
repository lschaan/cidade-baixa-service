package com.lschaan.cidadebaixa.dto;

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
public class SymplaDTO {
  private String name;
  private String startDate;
  private String description;
  private String detailsUrl;
}
