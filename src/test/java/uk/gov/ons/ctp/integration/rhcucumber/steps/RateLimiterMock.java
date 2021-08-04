package uk.gov.ons.ctp.integration.rhcucumber.steps;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.common.rest.RestClientConfig;

@Data
@NoArgsConstructor
@Component
public class RateLimiterMock {

  @Value("${envoy.host}")
  private String envoyHost;

  @Value("${envoy.port}")
  private String envoyPort;

  @Value("${envoy.scheme}")
  private String envoyScheme;

  public RestClient rateLimiterMock() throws CTPException {
    int connectionManagerDefaultMaxPerRoute = 20;
    int connectionManagerMaxTotal = 50;

    int connectTimeoutMillis = 0;
    int connectionRequestTimeoutMillis = 0;
    int socketTimeoutMillis = 0;

    RestClientConfig restClientConfig =
        new RestClientConfig(
            envoyScheme,
            envoyHost,
            envoyPort,
            "",
            "",
            connectionManagerDefaultMaxPerRoute,
            connectionManagerMaxTotal,
            connectTimeoutMillis,
            connectionRequestTimeoutMillis,
            socketTimeoutMillis);
    return new RestClient(restClientConfig);
  }
}
