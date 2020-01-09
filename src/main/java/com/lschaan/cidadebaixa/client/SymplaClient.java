package com.lschaan.cidadebaixa.client;

import com.lschaan.cidadebaixa.helper.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;

@Service
public class SymplaClient {
  private static final Logger logger = LoggerFactory.getLogger(SymplaClient.class);

  @Autowired private Client client;

  public String findFromSympla(Integer id) {
    logger.info("Sending request for sympla informations for id {}", id);
    String idString = "id=" + id;
    return client.findFromSympla(
        Constants.DEFAULT_REQUESTED_WITH,
        Constants.DEFAULT_USER_AGENT,
        MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        idString);
  }

  public String findDetailsFromSympla(String url) {
    return client.findDetailsFromSympla(
        URI.create(url),
        Constants.DEFAULT_REQUESTED_WITH,
        Constants.DEFAULT_USER_AGENT,
        MediaType.APPLICATION_FORM_URLENCODED_VALUE);
  }

  @FeignClient(name = "sympla-service", url = Constants.SYMPLA_URL)
  interface Client {
    @PostMapping(
        value = "/site/futureUserPageEvents",
        produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String findFromSympla(
        @RequestHeader("x-requested-with") String requestedWith,
        @RequestHeader("user-agent") String userAgent,
        @RequestHeader("Content-Type") String contentType,
        @RequestBody String body);

    @GetMapping
    String findDetailsFromSympla(
        URI baseUrl,
        @RequestHeader("x-requested-with") String requestedWith,
        @RequestHeader("user-agent") String userAgent,
        @RequestHeader("Content-Type") String contentType);
  }
}
