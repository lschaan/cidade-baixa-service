package com.lschaan.cidadebaixa.api;

import com.lschaan.cidadebaixa.api.response.PartyResponse;
import com.lschaan.cidadebaixa.service.CidadeBaixaService;
import com.lschaan.cidadebaixa.type.ClubEnum;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
public class CidadeBaixaApi {
  @Autowired private CidadeBaixaService cidadeBaixaService;

  @GetMapping("/list")
  @ApiOperation(value = "Get parties from cidade-baixa", response = PartyResponse.class)
  public ResponseEntity<?> getParties(
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
      @RequestParam(required = false) ClubEnum club,
      @RequestParam(required = false) Double maxValue) {
    return ResponseEntity.ok(
        PartyResponse.builder()
            .content(cidadeBaixaService.getParties(date, club, maxValue))
            .build());
  }
}
