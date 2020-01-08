package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NuvemService extends SymplaService implements ClubService {
  private static final Logger logger = LoggerFactory.getLogger(NuvemService.class);

  @Autowired private SymplaService symplaService;

  @Override
  public List<PartyDTO> getParties(LocalDate date, Double maxValue) {
    logger.info(
        "Request from nuvem to sympla for party list, date {}, maxValue {}", date, maxValue);
    return symplaService.getParties(ClubEnum.NUVEM, date, maxValue);
  }
}
