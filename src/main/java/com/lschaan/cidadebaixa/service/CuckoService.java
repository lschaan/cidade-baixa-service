package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.client.CuckoClient;
import com.lschaan.cidadebaixa.client.response.CuckoResponse;
import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.dto.TicketDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;
import com.lschaan.cidadebaixa.type.TicketEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lschaan.cidadebaixa.helper.Constants.ISO_DATE_TIME_FORMAT;

@Service
public class CuckoService implements ClubService {
  private static final Logger logger = LoggerFactory.getLogger(CuckoService.class);

  @Autowired private CuckoClient cuckoClient;

  @Override
  public List<PartyDTO> getParties(LocalDate date, Double maxValue) {
    logger.info("Getting party list from cucko service, date: {}, maxValue: {}", date, maxValue);
    return cuckoClient.getAll().stream()
        .map(response -> createParty(response, maxValue))
        .filter(party -> date == null || party.getDate().isEqual(date))
        .filter(party -> !party.getTickets().isEmpty())
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
    List<TicketDTO> tickets = new ArrayList<>();
    tickets.add(
        TicketDTO.builder()
            .price(response.getPriceInAdvance())
            .type(TicketEnum.IN_ADVANCE)
            .dueDate(date)
            .build());

    tickets.add(
        TicketDTO.builder()
            .price(response.getPriceOnSite())
            .type(TicketEnum.ON_SITE)
            .dueDate(date)
            .build());

    return maxValue == null
        ? tickets
        : tickets.stream()
            .filter(ticket -> ticket.getPrice() <= maxValue)
            .collect(Collectors.toList());
  }

  private LocalDate getDate(CuckoResponse party) {
    return LocalDate.parse(party.getDate(), DateTimeFormatter.ofPattern(ISO_DATE_TIME_FORMAT));
  }
}
