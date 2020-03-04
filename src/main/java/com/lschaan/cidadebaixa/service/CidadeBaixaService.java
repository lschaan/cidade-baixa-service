package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;
import com.lschaan.cidadebaixa.validator.PartyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.lschaan.cidadebaixa.validator.PartyValidator.isOnDate;

@Service
public class CidadeBaixaService {
  private static final Logger logger = LoggerFactory.getLogger(CidadeBaixaService.class);

  @Autowired private SymplaService symplaService;
  @Autowired private ApplicationContext context;

  public List<PartyDTO> getParties(ClubEnum club, LocalDate date, Double maxValue) {
    logger.info(
        "Started service to get parties. date: {}, club: {}, max value: {}", date, club, maxValue);

    List<PartyDTO> partyList =
        club == null ? getAllParties(date, maxValue) : getPartiesFromClub(club, date, maxValue);

    return partyList.stream()
        .filter(party -> isOnDate(party, date))
        .filter(PartyValidator::hasTickets)
        .sorted(Comparator.comparing(PartyDTO::getDate))
        .collect(Collectors.toList());
  }

  private List<PartyDTO> getAllParties(LocalDate date, Double maxValue) {
    logger.info("No club filters found, searching for all club parties");
    return Arrays.stream(ClubEnum.values())
        .flatMap(club -> getPartiesFromClub(club, date, maxValue).stream())
        .collect(Collectors.toList());
  }

  private List<PartyDTO> getPartiesFromClub(ClubEnum club, LocalDate date, Double maxValue) {
    return club.getIdSympla() == null
        ? context.getBean(club.toString(), ClubService.class).getParties(date, maxValue)
        : symplaService.getParties(club, date, maxValue);
  }
}
