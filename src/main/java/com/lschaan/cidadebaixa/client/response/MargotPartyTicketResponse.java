package com.lschaan.cidadebaixa.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MargotPartyTicketResponse {
  private Integer id;

  @JsonProperty("unitary")
  private Double price;
}
