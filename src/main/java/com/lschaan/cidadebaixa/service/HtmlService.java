package com.lschaan.cidadebaixa.service;

import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.lschaan.cidadebaixa.dto.SymplaDTO;
import com.lschaan.cidadebaixa.dto.TicketDTO;
import com.lschaan.cidadebaixa.helper.Constants;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import static org.jsoup.Jsoup.parse;

@Service
public class HtmlService {

  private static final Integer PRICE_INDEX = 1;
  private static final Integer DATE_INDEX = 3;
  private static final Integer DATE_START_INDEX = 11;
  private static final Integer DATE_END_INDEX = 21;

  public List<String> getUrlListFromSympla(String html) {
    return parse(html).body().getElementsByClass("event-box event-box-245xs-290lg sympla-card")
        .stream()
        .map(x -> x.getElementsByClass("event-box-link").attr("href"))
        .collect(Collectors.toList());
  }

  public SymplaDTO getPartyFromSympla(String html) {
    try {
      Object json =
          JsonUtils.fromString(
              Jsoup.parse(html).tagName("script").select("[type=application/ld+json]").html());
      Map jsonMap = JsonLdProcessor.compact(json, new HashMap<>(), new JsonLdOptions());
      return SymplaDTO.builder()
          .description((String) jsonMap.get("http://schema.org/description"))
          .name((String) jsonMap.get("http://schema.org/name"))
          .startDate((String) ((Map) jsonMap.get("http://schema.org/startDate")).get("@value"))
          .detailsUrl(
              (String)
                  ((Map)
                          ((Map) jsonMap.get("http://schema.org/offers"))
                              .get("http://schema.org/url"))
                      .get("@id"))
          .build();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<TicketDTO> getTicketsFromSympla(String html) {
    return Jsoup.parse(html).getElementById("ticket-form").getElementsByTag("td").not(".opt-panel")
        .stream()
        .map(
            x -> {
              List<String> elements = x.getElementsByTag("span").eachText();
              return TicketDTO.builder()
                  .dueDate(
                      LocalDate.parse(
                          elements.get(DATE_INDEX).substring(DATE_START_INDEX, DATE_END_INDEX),
                          DateTimeFormatter.ofPattern(Constants.BR_DATE_PATTERN)))
                  .price(
                      Double.valueOf(
                          elements
                              .get(PRICE_INDEX)
                              .replace(" ", "")
                              .replace("R$", "")
                              .replace(",", ".")))
                  .build();
            })
        .collect(Collectors.toList());
  }
}
