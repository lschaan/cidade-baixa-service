package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.dto.PartyDTO;
import com.lschaan.cidadebaixa.dto.TicketDTO;
import com.lschaan.cidadebaixa.type.ClubEnum;
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

    when(cuckoService.getParties(any(), any()))
        .thenReturn(Collections.singletonList(mockParty(CUCKO)));
    when(symplaService.getParties(any(), any(), any()))
        .thenReturn(Collections.singletonList(mockParty(NUVEM)));
    when(margotService.getParties(any(), any()))
        .thenReturn(Collections.singletonList(mockParty(MARGOT)));
  }

  @Test
  public void shouldReturnPartyList_whenNotFiltered() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(null, null, null);
    Assert.assertEquals(mockPartyList().toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByDate() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(null, today, null);
    Assert.assertEquals(mockPartyList().toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByMaxValue() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(null, null, 20.0);
    Assert.assertEquals(mockPartyList().toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByClub() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(CUCKO, null, null);
    Assert.assertEquals(
        Collections.singletonList(mockParty(CUCKO)).toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByClubAndDate() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(CUCKO, today, null);
    Assert.assertEquals(
        Collections.singletonList(mockParty(CUCKO)).toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByClubAndMaxValue() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(CUCKO, null, 20.0);
    Assert.assertEquals(
        Collections.singletonList(mockParty(CUCKO)).toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByDateAndMaxValue() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(null, today, 20.0);
    Assert.assertEquals(mockPartyList().toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenGivenAllParameters() {
    List<PartyDTO> partyList = cidadeBaixaService.getParties(CUCKO, today, 20.0);
    Assert.assertEquals(
        Collections.singletonList(mockParty(CUCKO)).toString(), partyList.toString());
  }

  @Test
  public void shouldSortPartiesByDate_whenGivenNoClubFilters() {
    when(symplaService.getParties(any(), any(), any())).thenReturn(Collections.emptyList());
    when(margotService.getParties(any(), any())).thenReturn(Collections.emptyList());

    when(cuckoService.getParties(any(), any()))
        .thenReturn(Arrays.asList(mockParty(CUCKO, tomorrow), mockParty(CUCKO)));
    List<PartyDTO> partyList = cidadeBaixaService.getParties(null, today, null);
    Assert.assertEquals(
        Arrays.asList(mockParty(CUCKO), mockParty(CUCKO, tomorrow)).toString(),
        partyList.toString());
  }

  private List<PartyDTO> mockPartyList() {
    return Arrays.asList(mockParty(CUCKO), mockParty(NUVEM), mockParty(MARGOT));
  }

  private PartyDTO mockParty(ClubEnum club) {
    return PartyDTO.builder()
        .club(club)
        .date(today)
        .tickets(Collections.singletonList(mockTicket()))
        .build();
  }

  private PartyDTO mockParty(ClubEnum club, LocalDate date) {
    return PartyDTO.builder()
        .club(club)
        .date(date)
        .tickets(Collections.singletonList(mockTicket()))
        .build();
  }

  private TicketDTO mockTicket() {
    return TicketDTO.builder().price(10.5).build();
  }
}
