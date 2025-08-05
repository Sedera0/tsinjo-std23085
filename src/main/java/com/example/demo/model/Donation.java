package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
public class Donation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne private Donor donor;

  @OneToOne(cascade = CascadeType.ALL)
  private Payment payment;

  private LocalDateTime createdAt = LocalDateTime.now();
}
