package com.lschaan.cidadebaixa.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ClubEnum {
  CUCKO,
  NUVEM(105574),
  OCIDENTE(1952167),
  MARGOT;

  private Integer idSympla;
}
