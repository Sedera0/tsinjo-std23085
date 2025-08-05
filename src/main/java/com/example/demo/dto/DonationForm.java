package com.example.demo.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DonationForm {
  private String email;
  private String fullName;
  private BigDecimal amount;
  private String paymentMethod;
}
