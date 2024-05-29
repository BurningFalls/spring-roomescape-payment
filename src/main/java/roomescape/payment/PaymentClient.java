package roomescape.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import roomescape.payment.dto.PaymentRequest;

import java.util.Base64;

// TODO: 인터페이스로 분리
public class PaymentClient {

    @Value("${payments.toss.secret-key}")
    private String secretKey;
    @Value("${payments.toss.password}")
    private String password;

    private final RestClient restClient;

    public PaymentClient(final HttpComponentsClientHttpRequestFactory factory) {
        this.restClient = RestClient.builder()
                .requestFactory(factory)
                .baseUrl("https://api.tosspayments.com")
                .build();
    }

    public ResponseEntity<Void> postPayment(final PaymentRequest paymentRequest) {
        final String secret = "Basic " + Base64.getEncoder().encodeToString((secretKey+password).getBytes());
        
        return restClient.post()
                .uri("/v1/payments/confirm")
                .header("Authorization", secret)
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentRequest)
                .retrieve()
                .toBodilessEntity();
    }
}