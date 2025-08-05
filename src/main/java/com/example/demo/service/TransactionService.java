package com.example.demo.service;

import com.example.demo.dto.DonationForm;
import com.example.demo.model.Donation;
import com.example.demo.model.Donor;
import com.example.demo.model.Help;
import com.example.demo.model.Payment;
import com.example.demo.repository.DonationRepository;
import com.example.demo.repository.DonorRepository;
import com.example.demo.repository.HelpRepository;
import com.example.demo.utils.PaymentStatus;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
  private final DonationRepository donationRepository;
  private final HelpRepository helpRepository;
  private final VolaService volaService;
  private final DonorRepository donorRepository;

  public List<Object> getAllTransactionsOrderedByDate() {
    List<Donation> donations = donationRepository.findAllByOrderByCreatedAtDesc();
    List<Help> helps = helpRepository.findAllByOrderByCreatedAtDesc();

    return Stream.concat(donations.stream(), helps.stream())
        .sorted(
            Comparator.comparing(
                    t ->
                        t instanceof Donation
                            ? ((Donation) t).getCreatedAt()
                            : ((Help) t).getCreatedAt())
                .reversed())
        .collect(Collectors.toList());
  }

  public void processDonation(DonationForm form) {
    String pspPaymentId = "TSINJO_" + UUID.randomUUID().toString();

    Donor donor = new Donor();
    donor.setEmail(form.getEmail());
    donor.setFullName(form.getFullName());
    donor = donorRepository.save(donor);
    final Donor finalDonor = donor;

    volaService
        .createPayment(form, pspPaymentId)
        .flatMap(
            volaPayment -> {
              Payment payment = new Payment();
              payment.setId(volaPayment.getId());
              payment.setPspType(form.getPaymentMethod());
              payment.setAmount(form.getAmount().multiply(BigDecimal.valueOf(100)).intValue());
              payment.setVerificationStatus(
                  PaymentStatus.valueOf(volaPayment.getVerificationStatus().toString()));

              Donation donation = new Donation();
              donation.setDonor(finalDonor);
              donation.setPayment(payment);

              return Mono.just(donationRepository.save(donation));
            })
        .subscribe(
            donation -> log.info("Donation processed: {}", donation.getId()),
            error -> log.error("Donation processing failed", error));
  }
}
