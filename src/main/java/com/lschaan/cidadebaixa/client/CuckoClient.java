package com.lschaan.cidadebaixa.client;

import com.lschaan.cidadebaixa.client.response.CuckoResponse;
import com.lschaan.cidadebaixa.helper.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Component
public class CuckoClient {
  private static final Logger logger = LoggerFactory.getLogger(CuckoClient.class);

  @Autowired private Client client;

  public List<CuckoResponse> getAll() {
    logger.info("Sending feign request to cucko-api for next events");
    return client.getAll(Constants.USER_AGENT_HEADER);
  }

  @FeignClient(name = "cucko-service", url = Constants.CUCKO_URL)
  interface Client {
    @GetMapping(value = "/api/v1/event")
    List<CuckoResponse> getAll(@RequestHeader("User-Agent") String userAgent);
  }
}
