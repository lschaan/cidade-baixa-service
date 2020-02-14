package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.client.CuckoClient;
import com.lschaan.cidadebaixa.client.response.CuckoResponse;
import com.lschaan.cidadebaixa.dto.PartyDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.lschaan.cidadebaixa.helper.Constants.ISO_DATE_TIME_FORMAT;
import static com.lschaan.cidadebaixa.stub.PartyDTOStub.mockPartyDto;
import static com.lschaan.cidadebaixa.stub.PartyDTOStub.mockPartyDtoWithSingleTicket;
import static com.lschaan.cidadebaixa.stub.PartyDTOStub.mockPartyList;
import static com.lschaan.cidadebaixa.stub.PartyDTOStub.mockSingletonPartyListWithSingleTicket;
import static com.lschaan.cidadebaixa.type.ClubEnum.CUCKO;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CuckoServiceTest {

  @Mock private CuckoClient cuckoClient;
  @InjectMocks private CuckoService cuckoService;

  private LocalDate today;
  private LocalDate tomorrow;

  @Before
  public void init() {
    this.today = LocalDate.now();
    this.tomorrow = today.plusDays(1);

    when(cuckoClient.getAll()).thenReturn(mockCuckoResponseList());
  }

  @Test
  public void shouldReturnPartyList_whenGivenNoParameters() {
    List<PartyDTO> partyList = cuckoService.getParties(null, null);
    Assert.assertEquals(mockPartyList(CUCKO, today, tomorrow).toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByValue() {
    List<PartyDTO> partyList = cuckoService.getParties(null, 10.0);
    Assert.assertEquals(
        Arrays.asList(
                mockPartyDtoWithSingleTicket(CUCKO, today, 10.0),
                mockPartyDtoWithSingleTicket(CUCKO, tomorrow, 10.0))
            .toString(),
        partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByDate() {
    List<PartyDTO> partyList = cuckoService.getParties(today, null);
    Assert.assertEquals(
        Collections.singletonList(mockPartyDto(CUCKO, today)).toString(), partyList.toString());
  }

  @Test
  public void shouldReturnPartyList_whenFilteredByDateAndValue() {
    List<PartyDTO> partyList = cuckoService.getParties(today, 10.0);
    Assert.assertEquals(
        mockSingletonPartyListWithSingleTicket(CUCKO, today, 10.0).toString(),
        partyList.toString());
  }

  @Test
  public void shouldReturnEmptyList_whenFilteredByMaxValue() {
    List<PartyDTO> partyList = cuckoService.getParties(null, 5.0);
    Assert.assertEquals(Collections.emptyList().toString(), partyList.toString());
  }

  @Test
  public void shouldReturnEmptyList_whenFilteredByDate() {
    List<PartyDTO> partyList = cuckoService.getParties(today.minusDays(1), null);
    Assert.assertEquals(Collections.emptyList().toString(), partyList.toString());
  }

  private List<CuckoResponse> mockCuckoResponseList() {
    return Arrays.asList(mockCuckoResponse(today), mockCuckoResponse(tomorrow));
  }

  private CuckoResponse mockCuckoResponse(LocalDate date) {
    return CuckoResponse.builder()
        .name("party")
        .date(date.atStartOfDay().format(DateTimeFormatter.ofPattern(ISO_DATE_TIME_FORMAT)))
        .priceInAdvance(10.0)
        .priceOnSite(20.0)
        .message("description")
        .build();
  }
}
