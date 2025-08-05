package com.example.demo.repository;

import com.example.demo.model.Help;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelpRepository extends JpaRepository<Help, Long> {
  List<Help> findAllByOrderByCreatedAtDesc();
}
