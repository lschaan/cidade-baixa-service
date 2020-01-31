package com.lschaan.cidadebaixa.validator;

import com.lschaan.cidadebaixa.dto.TicketDTO;
import org.springframework.stereotype.Service;

@Service
public class TicketValidator {

  public static boolean isOnPriceRange(TicketDTO ticket, Double maxValue) {
    return maxValue == null || ticket.getPrice() <= maxValue;
  }
}
