package com.lschaan.cidadebaixa.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MargotPartyResponse {
  private Integer id;
  private String name;

  @JsonProperty(value = "start")
  private String date;

  @JsonProperty("description")
  private String message;

  @JsonProperty("active_batches")
  private List<MargotPartyTicketResponse> tickets;
}
