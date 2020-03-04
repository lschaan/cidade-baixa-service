package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.dto.PartyDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.lschaan.cidadebaixa.stub.PartyDTOStub.mockPartyDtoWithSingleTicket;
import static com.lschaan.cidadebaixa.stub.PartyDTOStub.mockPartyList;
import static com.lschaan.cidadebaixa.stub.PartyDTOStub.mockPartyListWithSingleTicket;
import static com.lschaan.cidadebaixa.stub.PartyDTOStub.mockSingletonPartyList;
import static com.lschaan.cidadebaixa.stub.PartyDTOStub.mockSingletonPartyListWithSingleTicket;
import static com.lschaan.cidadebaixa.type.ClubEnum.CUCKO;
import static com.lschaan.cidadebaixa.type.ClubEnum.MARGOT;
import static com.lschaan.cidadebaixa.type.ClubEnum.NUVEM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CidadeBaixaServiceTest {

  @Mock private SymplaService symplaService;
  @Mock private ApplicationContext context;
  @Mock private CuckoService cuckoService;
  @Mock private MargotService margotService;
  @InjectMocks private CidadeBaixaService cidadeBaixaService;

  private LocalDate today;
  private LocalDate tomorrow;

  @Before
  public void init() {
    this.today = LocalDate.now();
    this.tomorrow = today.plusDays(1);

    when(context.getBean(eq(CUCKO.toString()), eq(ClubService.class))).thenReturn(cuckoService);
    when(context.getBean(eq(MARGOT.toString()), eq(ClubService.class))).thenReturn(margotService);

    when(cuckoService.getParties(any(), any())).thenReturn(mockSingletonPartyList(CUCKO, today));
    when(symplaService.getParties(any(), any(), any()))
        .thenReturn(mockSingletonPartyList(NUVEM, today));
    when(margotService.getParties(any(), any())).thenReturn(mockSingletonPartyList(MARGOT, today));
  }

  @Test
  public void shouldReturnPartyList_whenNotFiltered() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(null, null, null);
    Assert.assertEquals(mockPartyList(today).toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByDate() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(null, today, null);
    Assert.assertEquals(mockPartyList(today).toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByMaxValue() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(null, null, 20.0);
    Assert.assertEquals(mockPartyList(today).toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByClub() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(CUCKO, null, null);
    Assert.assertEquals(mockSingletonPartyList(CUCKO, today).toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByClubAndDate() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(CUCKO, today, null);
    Assert.assertEquals(mockSingletonPartyList(CUCKO, today).toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByClubAndMaxValue() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(CUCKO, null, 20.0);
    Assert.assertEquals(mockSingletonPartyList(CUCKO, today).toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByDateAndMaxValue() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(null, today, 20.0);
    Assert.assertEquals(mockPartyList(today).toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenGivenAllParameters() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(CUCKO, today, 20.0);
    Assert.assertEquals(mockSingletonPartyList(CUCKO, today).toString(), partyList.toString());
  }

  @Test
  public void shouldSortPartiesByDate_whenGivenNoClubFilters() {
    when(symplaService.getParties(any(), any(), any())).thenReturn(Collections.emptyList());
    when(margotService.getParties(any(), any())).thenReturn(Collections.emptyList());

    when(cuckoService.getParties(any(), any()))
        .thenReturn(
            Arrays.asList(
                mockPartyDtoWithSingleTicket(CUCKO, tomorrow, 20.0),
                mockPartyDtoWithSingleTicket(CUCKO, today, 10.0)));
    List<PartyDTO> partyList = cidadeBaixaService.getParties(null, today, null);
    Assert.assertEquals(
        mockSingletonPartyListWithSingleTicket(CUCKO, today, 10.0).toString(), partyList.toString());
  }

  @Test
  public void shouldFilterPartyList_whenFilteredByDate () {
    when(cuckoService.getParties(any(), any())).thenReturn(mockPartyListWithSingleTicket(CUCKO, today, tomorrow));
    List<PartyDTO> partyList = cidadeBaixaService.getParties(CUCKO, today, null);
    Assert.assertEquals(mockSingletonPartyListWithSingleTicket(CUCKO, today, 10.0).toString(), partyList.toString());
  }
}
