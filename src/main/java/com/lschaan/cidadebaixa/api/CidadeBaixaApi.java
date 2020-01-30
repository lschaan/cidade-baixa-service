package com.lschaan.cidadebaixa.api;

import com.lschaan.cidadebaixa.api.response.PartyResponse;
import com.lschaan.cidadebaixa.service.CidadeBaixaService;
import com.lschaan.cidadebaixa.type.ClubEnum;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static com.lschaan.cidadebaixa.helper.Constants.ISO_DATE_FORMAT;

@RestController
@RequestMapping("api/v1/")
public class CidadeBaixaApi {
  private static final Logger logger = LoggerFactory.getLogger(CidadeBaixaApi.class);

  @Autowired private CidadeBaixaService cidadeBaixaService;

  @GetMapping("/list")
  @ApiOperation(value = "Get parties from cidade-baixa", response = PartyResponse.class)
  public ResponseEntity<?> getParties(
      @RequestParam(required = false) ClubEnum club,
      @RequestParam(required = false) @ApiParam(value = ISO_DATE_FORMAT) @DateTimeFormat(pattern = ISO_DATE_FORMAT) LocalDate date,
      @RequestParam(required = false) Double maxValue) {
    logger.info("Starting api to find party list.");
    logger.info("Date: {}, Club: {}, Max value: {}", date, club, maxValue);
    return ResponseEntity.ok(
        PartyResponse.builder()
            .content(cidadeBaixaService.getParties(club, date, maxValue))
            .build());
  }
}
