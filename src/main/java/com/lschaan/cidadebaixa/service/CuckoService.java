package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.client.CuckoClient;
import com.lschaan.cidadebaixa.client.response.CuckoResponse;
import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.dto.TicketDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;
import com.lschaan.cidadebaixa.type.TicketEnum;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuckoService implements ClubService {
  private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

  @Autowired private CuckoClient cuckoClient;

  @Override
  public List<PartyDTO> getParties(LocalDate date, Double maxValue) {
    return cuckoClient.getAll().stream()
        .map(
            party ->
                PartyDTO.builder()
                    .partyName(party.getName())
                    .date(
                        LocalDate.parse(party.getDate(), DateTimeFormatter.ofPattern(DATE_PATTERN)))
                    .openBar(party.getMessage().contains("OPEN"))
                    .tickets(createTickets(party, maxValue))
                    .club(ClubEnum.CUCKO)
                    .build())
        .filter(x -> date == null || x.getDate().isEqual(date))
        .filter(x -> !x.getTickets().isEmpty())
        .collect(Collectors.toList());
  }

  private List<TicketDTO> createTickets(CuckoResponse response, Double maxValue) {
    List<TicketDTO> tickets = new ArrayList<>();

    tickets.add(
        TicketDTO.builder()
            .price(response.getPriceInAdvance())
            .type(TicketEnum.IN_ADVANCE)
            .build());

    tickets.add(
        TicketDTO.builder().price(response.getPriceOnSite()).type(TicketEnum.ON_SITE).build());

    return maxValue == null
        ? tickets
        : tickets.stream()
            .filter(ticket -> ticket.getPrice() <= maxValue)
            .collect(Collectors.toList());
  }
}
