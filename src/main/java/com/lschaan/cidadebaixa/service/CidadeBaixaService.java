package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CidadeBaixaService {
  private static final Logger logger = LoggerFactory.getLogger(CidadeBaixaService.class);
  private static final Map<ClubEnum, ClubService> partyMap = new HashMap<>();

  @Autowired private SymplaService symplaService;

  public CidadeBaixaService(CuckoService cuckoService) {
    partyMap.put(ClubEnum.CUCKO, cuckoService);
  }

  public List<PartyDTO> getParties(ClubEnum club, LocalDate date, Double maxValue) {
    logger.info(
        "Started main service to get party list. date: {}, club: {}, max value: {}",
        date,
        club,
        maxValue);

    return club == null
        ? getAllParties(date, maxValue)
        : club.getIdSympla() == null
            ? partyMap.get(club).getParties(date, maxValue)
            : symplaService.getParties(club, date, maxValue);
  }

  private List<PartyDTO> getAllParties(LocalDate date, Double maxValue) {
    logger.info("No club filters found, searching for all club parties");
    List<PartyDTO> partyList = getAllPartyMapParties(date, maxValue);
    partyList.addAll(getAllSymplaParties(date, maxValue));

    return partyList.stream()
        .sorted(Comparator.comparing(PartyDTO::getDate))
        .collect(Collectors.toList());
  }

  private List<PartyDTO> getAllPartyMapParties(LocalDate date, Double maxValue) {
    logger.info("Getting parties from partyMap {}", partyMap.keySet());
    return partyMap.values().stream()
        .map(partyService -> partyService.getParties(date, maxValue))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  private List<PartyDTO> getAllSymplaParties(LocalDate date, Double maxValue) {
    logger.info("Getting parties from clubs listed in sympla");
    return Arrays.stream(ClubEnum.values())
        .filter(x -> x.getIdSympla() != null)
        .map(club -> symplaService.getParties(club, date, maxValue))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
