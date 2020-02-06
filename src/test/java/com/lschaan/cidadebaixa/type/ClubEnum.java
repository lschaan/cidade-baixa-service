package com.lschaan.cidadebaixa.type;

public enum ClubEnum {
  CUCKO,
  NUVEM(105574),
  MARGOT;

  private Integer idSympla;

  ClubEnum(Integer idSympla) {
    this.idSympla = idSympla;
  }

  ClubEnum() {}

  public Integer getIdSympla() {
    return idSympla;
  }
}
