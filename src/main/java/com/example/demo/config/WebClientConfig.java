package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
  @Value("${vola.api.key}")
  private String apiKey;

  @Bean
  public WebClient volaWebClient() {
    return WebClient.builder()
        .baseUrl("https://42cwka3n4ifcp7ufheyrpmph240iuaxo.lambda-url.eu-west-3.on.aws")
        .defaultHeader("Authorization", "Bearer " + apiKey)
        .build();
  }
}
