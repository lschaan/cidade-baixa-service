package com.lschaan.cidadebaixa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lschaan.cidadebaixa.api.response.PartyResponse;
import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.dto.TicketDTO;
import com.lschaan.cidadebaixa.service.CidadeBaixaService;
import com.lschaan.cidadebaixa.type.ClubEnum;
import com.lschaan.cidadebaixa.type.TicketEnum;
import org.junit.Assert;
import org.junit.Before;
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
import java.util.Collections;
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

  @Autowired private ObjectMapper objectMapper;

  @Autowired private MockMvc mockMvc;

  @MockBean private CidadeBaixaService cidadeBaixaService;

  private LocalDate date;

  @Before
  public void init() {
    this.date = LocalDate.now();
    when(cidadeBaixaService.getParties(any(), any(), any())).thenReturn(mockEmptyPartyList());
  }

  @Test
  public void shouldReturnOK_whenGivenNoParameters() throws Exception {
    mockMvc.perform(get(URL)).andExpect(status().isOk());
  }

  @Test
  public void shouldReturnOK_whenGivenOnlyClubName() throws Exception {
    mockMvc.perform(get(URL).param("club", ClubEnum.CUCKO.toString())).andExpect(status().isOk());
  }

  @Test
  public void shouldReturnOK_whenGivenOnlyDate() throws Exception {
    mockMvc.perform(get(URL).param("date", date.toString())).andExpect(status().isOk());
  }

  @Test
  public void shouldReturnOK_whenGivenOnlyMaxValue() throws Exception {
    mockMvc.perform(get(URL).param("maxValue", String.valueOf(10.5))).andExpect(status().isOk());
  }

  @Test
  public void shouldReturnOK_whenGivenClubNameAndDate() throws Exception {
    mockMvc
        .perform(get(URL).param("club", ClubEnum.CUCKO.toString()).param("date", date.toString()))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldReturnOK_whenGivenClubNameAndMaxValue() throws Exception {
    mockMvc
        .perform(
            get(URL)
                .param("club", ClubEnum.CUCKO.toString())
                .param("maxValue", String.valueOf(10.5)))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldReturnOK_whenGivenDateAndMaxValue() throws Exception {
    mockMvc
        .perform(get(URL).param("date", date.toString()).param("maxValue", String.valueOf(10.5)))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldReturnOK_whenGivenAllParameters() throws Exception {
    mockMvc
        .perform(
            get(URL)
                .param("club", ClubEnum.CUCKO.toString())
                .param("date", date.toString())
                .param("maxValue", String.valueOf(10.5)))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldReturnPartyResponse() throws Exception {
    when(cidadeBaixaService.getParties(any(), any(), any()))
        .thenReturn(Collections.singletonList(mockPartyDto()));
    String expected =
        mockMvc
            .perform(get(URL))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    Assert.assertEquals(expected, objectMapper.writeValueAsString(mockPartyResponse()));
  }

  public void shouldReturnEmptyPartyResponse() throws Exception {
    when(cidadeBaixaService.getParties(any(), any(), any())).thenReturn(mockEmptyPartyList());
    String expected =
        mockMvc
            .perform(get(URL))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    Assert.assertEquals(expected, mockEmptyPartyResponse().toString());
  }

  private List<PartyDTO> mockEmptyPartyList() {
    return Collections.emptyList();
  }

  private PartyResponse mockEmptyPartyResponse() {
    return new PartyResponse(new ArrayList<>());
  }

  private TicketDTO mockTicketDto() {
    return TicketDTO.builder().dueDate(date).price(10.5).type(TicketEnum.ON_SITE).build();
  }

  private PartyDTO mockPartyDto() {
    return PartyDTO.builder()
        .partyName("Party")
        .date(date)
        .openBar(false)
        .tickets(Collections.singletonList(mockTicketDto()))
        .build();
  }

  private PartyResponse mockPartyResponse() {
    return new PartyResponse(Collections.singletonList(mockPartyDto()));
  }
}
