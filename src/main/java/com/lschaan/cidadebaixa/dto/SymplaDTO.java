package com.lschaan.cidadebaixa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"description", "detailsUrl", "html"})
public class SymplaDTO {
  private String name;
  private String startDate;
  private String description;
  private String detailsUrl;
  private String html;
}
