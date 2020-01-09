package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.dto.PartyDTO;

import java.time.LocalDate;
import java.util.List;

public interface ClubService {
  List<PartyDTO> getParties(LocalDate date, Double maxValue);
}
