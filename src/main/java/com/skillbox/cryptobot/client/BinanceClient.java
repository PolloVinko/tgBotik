package com.skillbox.cryptobot.client;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BinanceClient {
    private final HttpGet httpGet;
    private final ObjectMapper mapper;
    private final HttpClient httpClient;

    public BinanceClient(@Value("${binance.api.getPrice}") String uri) {
        httpGet = new HttpGet(uri);
        mapper = new ObjectMapper();
        httpClient = HttpClientBuilder.create()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
    }

    public BigDecimal getBitcoinPrice() throws IOException {
        log.info("Performing client call to binanceApi to get bitcoin price");
        try {
            BigDecimal price = new BigDecimal(mapper.readTree(EntityUtils.toString(httpClient.execute(httpGet).getEntity())).path("price").asDouble());
            return price;
        } catch (IOException e) {
            log.error("Error while getting price from binance", e);
            throw e;
        }
    }
}
