package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.client.SymplaClient;
import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.dto.SymplaDTO;
import com.lschaan.cidadebaixa.dto.TicketDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lschaan.cidadebaixa.helper.Constants.ISO_DATE_FORMAT;

@Service
public class SymplaService {
  private static final Logger logger = LoggerFactory.getLogger(SymplaService.class);

  private static final Integer DATE_START_INDEX = 0;
  private static final Integer DATE_END_INDEX = 10;

  @Autowired private SymplaClient symplaClient;

  @Autowired private HtmlService htmlService;

  protected List<PartyDTO> getParties(ClubEnum club, LocalDate date, Double maxValue) {
    logger.info("Getting sympla parties for club {}", club);
    try {
      return htmlService.getUrlListFromSympla(symplaClient.findFromSympla(club.getIdSympla()))
          .stream()
          .map(
              url -> {
                SymplaDTO symplaDTO =
                    htmlService.getPartyFromSympla(symplaClient.findDetailsFromSympla(url));
                return PartyDTO.builder()
                    .partyName(symplaDTO.getName())
                    .club(club)
                    .date(
                        LocalDate.parse(
                            symplaDTO.getStartDate().substring(DATE_START_INDEX, DATE_END_INDEX),
                            DateTimeFormatter.ofPattern(ISO_DATE_FORMAT)))
                    .openBar(symplaDTO.getDescription().contains("OPEN"))
                    .tickets(getTickets(symplaDTO, maxValue))
                    .build();
              })
          .filter(x -> date == null || x.getDate().isEqual(date))
          .filter(x -> !x.getTickets().isEmpty())
          .collect(Collectors.toList());
    } catch (Exception e) {
      logger.error("Unable to get parties from sympla with club {}. Returning empty list", club, e);
      return new ArrayList<>();
    }
  }

  private List<TicketDTO> getTickets(SymplaDTO symplaDTO, Double maxValue) {
    List<TicketDTO> tickets = htmlService.getTicketsFromSympla(symplaDTO.getHtml());
    return maxValue == null
        ? tickets
        : tickets.stream()
            .filter(ticket -> ticket.getPrice() <= maxValue)
            .collect(Collectors.toList());
  }
}
