package com.lschaan.cidadebaixa.stub;

import com.lschaan.cidadebaixa.client.response.CuckoResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static com.lschaan.cidadebaixa.helper.Constants.ISO_DATE_TIME_FORMAT;

public class CuckoResponseStub {

  public static List<CuckoResponse> mockCuckoResponseList(LocalDate today, LocalDate tomorrow) {
    return Arrays.asList(mockCuckoResponse(today), mockCuckoResponse(tomorrow));
  }

  public static CuckoResponse mockCuckoResponse(LocalDate date) {
    return CuckoResponse.builder()
        .name("party")
        .date(date.atStartOfDay().format(DateTimeFormatter.ofPattern(ISO_DATE_TIME_FORMAT)))
        .priceInAdvance(10.0)
        .priceOnSite(20.0)
        .message("description")
        .build();
  }
}
