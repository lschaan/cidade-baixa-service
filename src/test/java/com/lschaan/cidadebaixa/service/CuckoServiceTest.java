package com.lschaan.cidadebaixa.service;

import com.lschaan.cidadebaixa.client.CuckoClient;
import com.lschaan.cidadebaixa.dto.PartyDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.lschaan.cidadebaixa.stub.CuckoResponseStub.mockCuckoResponseList;
import static com.lschaan.cidadebaixa.stub.PartyDTOStub.mockPartyDtoWithSingleTicket;
import static com.lschaan.cidadebaixa.stub.PartyDTOStub.mockPartyList;
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

    when(cuckoClient.getAll()).thenReturn(mockCuckoResponseList(today, tomorrow));
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
}
