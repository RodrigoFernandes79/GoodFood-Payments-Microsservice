package com.goodfood.payments.controller;

import com.goodfood.payments.dto.PaymentDTO;
import com.goodfood.payments.service.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<Page<PaymentDTO>> listAllPayment(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok().body(paymentService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> findPaymentsById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok().body(paymentService.getPaymentById(id));
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> registerPayment(@RequestBody @Valid PaymentDTO paymentDTO, UriComponentsBuilder builder) {
        PaymentDTO payment = paymentService.createPayment(paymentDTO);
        URI uri = builder.path("/payments/").buildAndExpand(payment.getId()).toUri();
        return ResponseEntity.created(uri).body(payment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable @NotNull Long id, @RequestBody @Valid PaymentDTO paymentDTO) {
        return ResponseEntity.ok().body(paymentService.updatePayment(id, paymentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentDTO> deletePayment(@PathVariable @NotNull Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}/confirm-payment")
    @CircuitBreaker(name = "updateOrder", fallbackMethod = "paymentAuthorizedWithPendingIntegration")
    public void confirmPayment(@PathVariable @NotNull Long id) {

        paymentService.confirmPayment(id);
    }

    public void paymentAuthorizedWithPendingIntegration(Long id, Exception e) {
        paymentService.updateStatus(id);
    }
}

