package com.lschaan.cidadebaixa.client;

import com.lschaan.cidadebaixa.client.response.MargotResponse;
import com.lschaan.cidadebaixa.helper.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.lschaan.cidadebaixa.helper.Constants.MARGOT_URL;

@Component
public class MargotClient {
  private static final Logger logger = LoggerFactory.getLogger(MargotClient.class);

  @Autowired private Client client;

  public MargotResponse getAll() {
    logger.info("Sending feign request to margot-api for next events");
    return client.getAll(Constants.USER_AGENT_HEADER);
  }

  @FeignClient(name = "margot-service", url = MARGOT_URL)
  interface Client {
    @GetMapping(value = "/api/events/list-next")
    MargotResponse getAll(@RequestHeader("User-Agent") String userAgent);
  }
}
