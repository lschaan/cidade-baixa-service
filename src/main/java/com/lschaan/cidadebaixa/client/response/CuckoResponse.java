package com.lschaan.cidadebaixa.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "message")
public class CuckoResponse {
  private String date;
  private String name;

  @JsonProperty("release")
  private String message;

  @JsonProperty("price_full")
  private Double priceOnSite;

  @JsonProperty("price_site_list")
  private Double priceInAdvance;
}
