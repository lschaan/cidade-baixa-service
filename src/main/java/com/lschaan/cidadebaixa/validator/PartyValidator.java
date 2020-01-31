package com.lschaan.cidadebaixa.validator;

import com.lschaan.cidadebaixa.dto.PartyDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PartyValidator {

  public static boolean isOnDate(PartyDTO party, LocalDate date) {
    return date == null || party.getDate().isEqual(date);
  }

  public static boolean hasTickets(PartyDTO party) {
    return !party.getTickets().isEmpty();
  }
}
