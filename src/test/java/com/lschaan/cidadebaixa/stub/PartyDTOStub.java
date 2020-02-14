package com.lschaan.cidadebaixa.stub;

import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.dto.TicketDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.lschaan.cidadebaixa.type.ClubEnum.CUCKO;
import static com.lschaan.cidadebaixa.type.ClubEnum.MARGOT;
import static com.lschaan.cidadebaixa.type.ClubEnum.NUVEM;

public class PartyDTOStub {

  public static List<PartyDTO> mockPartyList(LocalDate date) {
    return Arrays.asList(
        mockPartyDto(CUCKO, date), mockPartyDto(NUVEM, date), mockPartyDto(MARGOT, date));
  }

  public static List<PartyDTO> mockPartyList(ClubEnum club, LocalDate today, LocalDate tomorrow) {
    return Arrays.asList(mockPartyDto(club, today), mockPartyDto(club, tomorrow));
  }

  public static List<PartyDTO> mockSingletonPartyListWithSingleTicket(
      ClubEnum club, LocalDate date, Double price) {
    return Collections.singletonList(mockPartyDtoWithSingleTicket(club, date, price));
  }

  public static List<PartyDTO> mockSingletonPartyList(ClubEnum club, LocalDate date) {
    return Collections.singletonList(mockPartyDto(club, date));
  }

  public static List<PartyDTO> mockPartyListWithSingleTicket(
      ClubEnum club, LocalDate today, LocalDate tomorrow) {
    return Arrays.asList(
        mockPartyDtoWithSingleTicket(club, today, 10.0),
        mockPartyDtoWithSingleTicket(club, tomorrow, 20.0));
  }

  public static PartyDTO mockPartyDto(ClubEnum club, LocalDate date) {
    return PartyDTO.builder()
        .partyName("party")
        .date(date)
        .club(club)
        .tickets(mockTicketList(date, 10.0, 20.0))
        .openBar(false)
        .build();
  }

  public static PartyDTO mockPartyDtoWithSingleTicket(ClubEnum club, LocalDate date, Double price) {
    return PartyDTO.builder()
        .partyName("party")
        .date(date)
        .club(club)
        .tickets(mockSingletonTicketList(price, date))
        .openBar(false)
        .build();
  }

  private static List<TicketDTO> mockSingletonTicketList(Double price, LocalDate date) {
    return Collections.singletonList(mockTicketDto(price, date));
  }

  private static List<TicketDTO> mockTicketList(
      LocalDate date, Double priceFirst, Double priceSecond) {
    return Arrays.asList(mockTicketDto(priceFirst, date), mockTicketDto(priceSecond, date));
  }

  private static TicketDTO mockTicketDto(Double price, LocalDate dueDate) {
    return TicketDTO.builder().price(price).dueDate(dueDate).build();
  }
}
