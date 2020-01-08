package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NuvemService extends SymplaService implements ClubService {

  @Autowired private SymplaService symplaService;

  @Override
  public List<PartyDTO> getParties(LocalDate date, Double maxValue) {
    return symplaService.getParties(ClubEnum.NUVEM, date, maxValue);
  }
}
