package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.client.CuckoClient;
import com.lschaan.cidadebaixa.client.response.CuckoResponse;
import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.dto.TicketDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;
import com.lschaan.cidadebaixa.validator.PartyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lschaan.cidadebaixa.helper.Constants.ISO_DATE_TIME_FORMAT;
import static com.lschaan.cidadebaixa.validator.PartyValidator.isOnDate;
import static com.lschaan.cidadebaixa.validator.TicketValidator.isOnPriceRange;

@Service("CUCKO")
public class CuckoService implements ClubService {
  private static final Logger logger = LoggerFactory.getLogger(CuckoService.class);

  @Autowired private CuckoClient cuckoClient;

  @Override
  public List<PartyDTO> getParties(LocalDate date, Double maxValue) {
    logger.info("Getting party list from cucko service, date: {}, maxValue: {}", date, maxValue);
    return cuckoClient.getAll().stream()
        .map(response -> createParty(response, maxValue))
        .filter(party -> isOnDate(party, date))
        .filter(PartyValidator::hasTickets)
        .collect(Collectors.toList());
  }

  private PartyDTO createParty(CuckoResponse response, Double maxValue) {
    LocalDate date = getDate(response);

    return PartyDTO.builder()
        .partyName(response.getName())
        .date(date)
        .openBar(response.getMessage().contains("OPEN"))
        .tickets(createTickets(response, date, maxValue))
        .club(ClubEnum.CUCKO)
        .build();
  }

  private List<TicketDTO> createTickets(CuckoResponse response, LocalDate date, Double maxValue) {
    return Stream.of(
            TicketDTO.builder().price(response.getPriceInAdvance()).dueDate(date).build(),
            TicketDTO.builder().price(response.getPriceOnSite()).dueDate(date).build())
        .filter(ticket -> isOnPriceRange(ticket, maxValue))
        .collect(Collectors.toList());
  }

  private LocalDate getDate(CuckoResponse party) {
    return LocalDate.parse(party.getDate(), DateTimeFormatter.ofPattern(ISO_DATE_TIME_FORMAT));
  }
}
