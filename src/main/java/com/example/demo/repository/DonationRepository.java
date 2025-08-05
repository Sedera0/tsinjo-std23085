package com.example.demo.repository;

import com.example.demo.model.Donation;
import com.example.demo.utils.PaymentStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long> {
  List<Donation> findByPaymentVerificationStatus(PaymentStatus status);

  List<Donation> findAllByOrderByCreatedAtDesc();
}
