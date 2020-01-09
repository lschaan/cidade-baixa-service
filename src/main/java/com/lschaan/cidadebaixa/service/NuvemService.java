package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NuvemService extends SymplaService implements ClubService {
  private static final Logger logger = LoggerFactory.getLogger(NuvemService.class);

  @Override
  public List<PartyDTO> getParties(LocalDate date, Double maxValue) {
    logger.info(
        "Request from nuvem to sympla for party list, date {}, maxValue {}", date, maxValue);
    return getParties(ClubEnum.NUVEM, date, maxValue);
  }
}
