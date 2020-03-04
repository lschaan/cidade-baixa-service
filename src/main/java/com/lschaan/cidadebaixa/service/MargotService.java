package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.client.MargotClient;
import com.lschaan.cidadebaixa.client.response.MargotPartyResponse;
import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.dto.TicketDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.lschaan.cidadebaixa.validator.TicketValidator.isOnPriceRange;

@Service("MARGOT")
public class MargotService implements ClubService {
  private static final Logger logger = LoggerFactory.getLogger(MargotService.class);

  @Autowired private MargotClient client;

  @Override
  public List<PartyDTO> getParties(LocalDate date, Double maxValue) {
    logger.info("Getting all parties from margot with date {} and maxValue {}", date, maxValue);
    return client.getAll().getData().stream()
        .map(response -> getParty(maxValue, response))
        .collect(Collectors.toList());
  }

  private PartyDTO getParty(Double maxValue, MargotPartyResponse response) {
    LocalDate date = getDate(response);
    return PartyDTO.builder()
        .club(ClubEnum.MARGOT)
        .date(date)
        .openBar(response.getName().contains("OPEN"))
        .partyName(response.getName())
        .tickets(getTickets(response, date, maxValue))
        .build();
  }

  private List<TicketDTO> getTickets(
      MargotPartyResponse response, LocalDate date, Double maxValue) {
    return response.getTickets().stream()
        .map(ticket -> TicketDTO.builder().price(ticket.getPrice()).dueDate(date).build())
        .filter(ticket -> isOnPriceRange(ticket, maxValue))
        .collect(Collectors.toList());
  }

  private LocalDate getDate(MargotPartyResponse response) {
    return LocalDate.parse(response.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }
}
