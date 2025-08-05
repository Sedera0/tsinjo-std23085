package com.example.demo.service;

import com.example.demo.dto.DonationForm;
import com.example.demo.model.Payment;
import com.example.demo.repository.DonationRepository;
import com.example.demo.utils.PaymentStatus;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
@Slf4j
public class VolaService {
  private final WebClient volaWebClient;
  private final DonationRepository donationRepository;

  public Mono<Payment> createPayment(DonationForm form, String pspPaymentId) {
    return volaWebClient
        .post()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/payment")
                    .queryParam("apiKey", "{apiKey}")
                    .queryParam("payerEmail", form.getEmail())
                    .queryParam("pspType", form.getPaymentMethod())
                    .queryParam("pspPaymentId", pspPaymentId)
                    .build())
        .retrieve()
        .bodyToMono(Payment.class)
        .timeout(Duration.ofSeconds(30))
        .doOnNext(payment -> log.info("Payment created: {}", payment.getId()))
        .doOnError(e -> log.error("Payment creation failed", e));
  }

  public Mono<Payment> verifyPayment(String paymentId) {
    return volaWebClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/payment")
                    .queryParam("apiKey", "{apiKey}")
                    .queryParam("pspPaymentId", paymentId)
                    .build())
        .retrieve()
        .bodyToMono(Payment.class)
        .timeout(Duration.ofSeconds(10))
        .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)));
  }

  @Scheduled(fixedRate = 60000)
  public void checkPendingPayments() {
    donationRepository
        .findByPaymentVerificationStatus(PaymentStatus.VERIFYING)
        .forEach(
            donation -> {
              verifyPayment(donation.getPayment().getId())
                  .subscribe(
                      volaPayment -> {
                        donation.getPayment().updateFromVolaResponse(volaPayment);
                        donationRepository.save(donation);
                      });
            });
  }
}
