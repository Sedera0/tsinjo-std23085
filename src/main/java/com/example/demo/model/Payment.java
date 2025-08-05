package com.example.demo.model;

import com.example.demo.utils.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.Data;

@Entity
@Data
public class Payment {
  @Id private String id;
  private String pspType;
  private Integer amount;
  private Instant creationInstant;
  private Instant lastPspVerificationInstant;
  private Integer verificationAttemptNb;

  @Enumerated(EnumType.STRING)
  private PaymentStatus verificationStatus;

  public void updateFromVolaResponse(Payment volaPayment) {
    this.verificationStatus = PaymentStatus.valueOf(volaPayment.getVerificationStatus().toString());
    this.lastPspVerificationInstant = Instant.now();
    if (verificationAttemptNb == null) {
      verificationAttemptNb = 1;
    } else {
      verificationAttemptNb++;
    }
  }
}
