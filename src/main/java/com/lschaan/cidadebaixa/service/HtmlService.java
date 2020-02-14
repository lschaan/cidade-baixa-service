package com.lschaan.cidadebaixa.service;

import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.lschaan.cidadebaixa.dto.SymplaDTO;
import com.lschaan.cidadebaixa.dto.TicketDTO;
import com.lschaan.cidadebaixa.helper.Constants;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jsoup.Jsoup.parse;

@Service
public class HtmlService {
  private static final Logger logger = LoggerFactory.getLogger(HtmlService.class);

  private static final Integer PRICE_INDEX = 1;
  private static final Integer DATE_INDEX = 3;
  private static final Integer DATE_START_INDEX = 11;
  private static final Integer DATE_END_INDEX = 21;

  private static final String DESCRIPTION = "http://schema.org/description";
  private static final String NAME = "http://schema.org/name";
  private static final String START_DATE = "http://schema.org/startDate";
  private static final String OFFERS = "http://schema.org/offers";
  private static final String URL = "http://schema.org/url";

  public List<String> extractUrlListFromSymplaHtml(String html) {
    return parse(html).body().getElementsByClass("event-box event-box-245xs-290lg sympla-card")
        .stream()
        .map(x -> x.getElementsByClass("event-box-link").attr("href"))
        .collect(Collectors.toList());
  }

  public SymplaDTO extractSymplaPartyFromHtml(String html) {
    try {
      Map<String, Object> jsonMap = getJsonMap(html);
      return SymplaDTO.builder()
          .description(getDescription(jsonMap))
          .name(getName(jsonMap))
          .startDate(getStartDate(jsonMap))
          .detailsUrl(getDetailsUrl(jsonMap))
          .html(html)
          .build();
    } catch (Exception e) {
      logger.error("Unable to get party details from sympla html", e);
      return new SymplaDTO();
    }
  }

  public List<TicketDTO> extractTicketListFromSymplaHtml(String html) {
    return getTicketListElements(html).stream()
        .map(
            element -> {
              List<String> elements = element.getElementsByTag("span").eachText();
              return TicketDTO.builder()
                  .dueDate(getDueDate(elements.get(DATE_INDEX)))
                  .price(getPrice(elements.get(PRICE_INDEX)))
                  .build();
            })
        .collect(Collectors.toList());
  }

  private Map<String, Object> getJsonMap(String html) throws IOException, JsonLdError {
    Object json =
        JsonUtils.fromString(
            Jsoup.parse(html).tagName("script").select("[type=application/ld+json]").html());
    return JsonLdProcessor.compact(json, new HashMap<>(), new JsonLdOptions());
  }

  private String getDescription(Map<String, Object> jsonMap) {
    return (String) jsonMap.get(DESCRIPTION);
  }

  private String getName(Map<String, Object> jsonMap) {
    return (String) jsonMap.get(NAME);
  }

  private String getStartDate(Map<String, Object> jsonMap) {
    return (String) ((Map<String, Object>) jsonMap.get(START_DATE)).get("@value");
  }

  private String getDetailsUrl(Map<String, Object> jsonMap) {
    return ((String)
        ((Map<String, Object>) ((Map<String, Object>) jsonMap.get(OFFERS)).get(URL)).get("@id"));
  }

  private Elements getTicketListElements(String html) {
    return Jsoup.parse(html).getElementById("ticket-form").getElementsByTag("td").not(".opt-panel");
  }

  private Double getPrice(String priceStr) {
    return Double.valueOf(priceStr.replace(" ", "").replace("R$", "").replace(",", "."));
  }

  private LocalDate getDueDate(String dueDateStr) {
    try {
      return LocalDate.parse(
          dueDateStr.substring(DATE_START_INDEX, DATE_END_INDEX),
          DateTimeFormatter.ofPattern(Constants.BR_DATE_PATTERN));
    } catch (Exception e) {
      logger.info("Unable to get date from {}, returning today's date.", dueDateStr);
      return LocalDate.now();
    }
  }
}
