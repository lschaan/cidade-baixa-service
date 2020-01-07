package com.lschaan.cidadebaixa.api.response;

import com.lschaan.cidadebaixa.dto.PartyDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PartyResponse {
    private List<PartyDTO> content;
}
