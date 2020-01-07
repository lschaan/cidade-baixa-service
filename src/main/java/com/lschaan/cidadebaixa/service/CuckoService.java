package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.client.CuckoClient;
import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuckoService implements PartyService {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private CuckoClient cuckoClient;

    @Override
    public List<PartyDTO> getParties(LocalDate date, Double maxValue) {
        return cuckoClient.getAll().stream().map(party -> PartyDTO
                .builder()
                .partyName(party.getName())
                .date(LocalDate.parse(party.getDate(), DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .openBar(party.getMessage().contains("OPEN"))
                .priceInAdvance(party.getPriceInAdvance())
                .priceOnSite(party.getPriceOnSite())
                .club(ClubEnum.CUCKO)
                .build())
                .filter(x -> date == null || x.getDate().isEqual(date))
                .filter(x -> maxValue == null || (x.getPriceInAdvance() <= maxValue || x.getPriceOnSite() <= maxValue))
                .collect(Collectors.toList());
    }
}
