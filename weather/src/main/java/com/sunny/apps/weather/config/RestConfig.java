package com.sunny.apps.weather.config;

import com.sunny.apps.weather.interceptor.ClientRequestInterceptor;
import com.sunny.apps.weather.interceptor.ClientRestTemplateCustomizer;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

@Configuration
public class RestConfig {

  @Value("${weather.rapid.api.base.url}")
  String baseUrl;

  @Bean
  public RestTemplate rapidAPITemplate(RestTemplateBuilder builder) {
    UriTemplateHandler uriTemplateHandler = new RootUriTemplateHandler(baseUrl);
    return builder
        .uriTemplateHandler(uriTemplateHandler)
        .setReadTimeout(Duration.ofMillis(10000))
        .build();
  }

  @Bean
  public ClientRequestInterceptor clientRequestInterceptor() {
    return new ClientRequestInterceptor();
  }

  @Bean
  @DependsOn("clientRequestInterceptor")
  public ClientRestTemplateCustomizer restTemplateCustomizer(
      ClientRequestInterceptor clientRequestInterceptor) {
    return new ClientRestTemplateCustomizer(clientRequestInterceptor);
  }

  @Bean
  @DependsOn("restTemplateCustomizer")
  public RestTemplateBuilder restTemplateBuilder(
      RestTemplateCustomizer restTemplateCustomizer) {
    return new RestTemplateBuilder(restTemplateCustomizer);
  }
}
