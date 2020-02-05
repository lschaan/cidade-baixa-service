package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CidadeBaixaService {
  private static final Logger logger = LoggerFactory.getLogger(CidadeBaixaService.class);

  @Autowired private SymplaService symplaService;
  @Autowired private ApplicationContext context;

  public List<PartyDTO> getParties(ClubEnum club, LocalDate date, Double maxValue) {
    logger.info(
        "Started service to get parties. date: {}, club: {}, max value: {}", date, club, maxValue);
    return club == null ? getAllParties(date, maxValue) : getPartiesFromClub(club, date, maxValue);
  }

  private List<PartyDTO> getAllParties(LocalDate date, Double maxValue) {
    logger.info("No club filters found, searching for all club parties");
    return Arrays.stream(ClubEnum.values())
        .map(club -> getPartiesFromClub(club, date, maxValue))
        .flatMap(Collection::stream)
        .sorted(Comparator.comparing(PartyDTO::getDate))
        .collect(Collectors.toList());
  }

  private List<PartyDTO> getPartiesFromClub(ClubEnum club, LocalDate date, Double maxValue) {
    return club.getIdSympla() == null
        ? context.getBean(club.toString(), ClubService.class).getParties(date, maxValue)
        : symplaService.getParties(club, date, maxValue);
  }
}
