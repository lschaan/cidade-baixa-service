package com.lschaan.cidadebaixa.client;

import com.lschaan.cidadebaixa.client.response.CuckoResponse;
import com.lschaan.cidadebaixa.helper.Constants;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
public class CuckoClient {
    @Autowired
    private Client client;

    public List<CuckoResponse> getAll() {
        return client.getAll(Constants.DEFAULT_USER_AGENT);
    }

    @FeignClient(name = "cucko-service", url = Constants.CUCKO_URL)
    interface Client {
        @GetMapping(value = "/api/v1/event")
        List<CuckoResponse> getAll(
                @RequestHeader("User-Agent") String userAgent);
    }
}
