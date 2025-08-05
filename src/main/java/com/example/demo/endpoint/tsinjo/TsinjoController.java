package com.example.demo.endpoint.tsinjo;

import com.example.demo.dto.DonationForm;
import com.example.demo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class TsinjoController {
  private final TransactionService transactionService;

  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("donationForm", new DonationForm());
    model.addAttribute("transactions", transactionService.getAllTransactionsOrderedByDate());
    return "index";
  }

  @PostMapping("/donate")
  public String handleDonation(DonationForm form) {
    transactionService.processDonation(form);
    return "redirect:/";
  }
}
