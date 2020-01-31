package com.lschaan.cidadebaixa.api;

import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.service.CidadeBaixaService;
import com.lschaan.cidadebaixa.type.ClubEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CidadeBaixaApi.class)
@ActiveProfiles("test")
public class CidadeBaixaApiTest {

  private static final String URL = "/api/v1/list";

  @Autowired private MockMvc mockMvc;

  @MockBean private CidadeBaixaService cidadeBaixaService;

  @Test
  public void shouldReturnOK_whenGivenNoParameters() throws Exception {
    when(cidadeBaixaService.getParties(any(), any(), any())).thenReturn(mockEmptyPartyList());
    mockMvc.perform(get(URL)).andExpect(status().isOk());
  }

  @Test
  public void shouldReturnOK_whenGivenOnlyClubName() throws Exception {
    when(cidadeBaixaService.getParties(any(), any(), any())).thenReturn(mockEmptyPartyList());
    mockMvc.perform(get(URL).param("club", ClubEnum.CUCKO.toString())).andExpect(status().isOk());
  }

  @Test
  public void shouldReturnOK_whenGivenOnlyDate() throws Exception {
    when(cidadeBaixaService.getParties(any(), any(), any())).thenReturn(mockEmptyPartyList());
    mockMvc.perform(get(URL).param("date", LocalDate.now().toString())).andExpect(status().isOk());
  }

  @Test
  public void shouldReturnOK_whenGivenOnlyMaxValue() throws Exception {
    when(cidadeBaixaService.getParties(any(), any(), any())).thenReturn(mockEmptyPartyList());
    mockMvc.perform(get(URL).param("maxValue", String.valueOf(10))).andExpect(status().isOk());
  }

  private List<PartyDTO> mockEmptyPartyList() {
    return new ArrayList<>();
  }
}
