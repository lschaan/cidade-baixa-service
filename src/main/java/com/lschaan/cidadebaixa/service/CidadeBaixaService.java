package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CidadeBaixaService {
    private static Map<ClubEnum, PartyService> partyMap = new HashMap<>();

    public CidadeBaixaService(CuckoService cuckoService) {
        partyMap.put(ClubEnum.CUCKO, cuckoService);
    }

    public List<PartyDTO> getParties(LocalDate date, ClubEnum club, Double maxValue) {
        return club == null ? getAllParties(date, maxValue) : partyMap.get(club).getParties(date, maxValue);
    }

    private List<PartyDTO> getAllParties(LocalDate date, Double maxValue) {
        return partyMap.values().stream()
                .map(partyService -> partyService.getParties(date, maxValue))
                .reduce(new ArrayList<>(), (list, listAll) -> {
                    listAll.addAll(list);
                    return listAll;
                });
    }
}
