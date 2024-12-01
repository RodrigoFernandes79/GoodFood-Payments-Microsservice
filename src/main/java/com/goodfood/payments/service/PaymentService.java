package com.goodfood.payments.service;

import com.goodfood.payments.dto.PaymentDTO;
import com.goodfood.payments.http.OrderClient;
import com.goodfood.payments.model.Payment;
import com.goodfood.payments.model.Status;
import com.goodfood.payments.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OrderClient orderClient;

    public Page<PaymentDTO> findAll(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(p -> modelMapper.map(p, PaymentDTO.class));
    }

    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        PaymentDTO dto = modelMapper.map(payment, PaymentDTO.class);
        dto.setItens(orderClient.returnOrdersInPayment(payment.getOrderId()).getItens());
        return dto;
    }

    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setStatus(Status.CREATED);
        paymentRepository.save(payment);
        return modelMapper.map(payment, PaymentDTO.class);
    }

    public PaymentDTO updatePayment(Long id, PaymentDTO paymentDTO) {
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setId(id);
        paymentRepository.save(payment);
        return modelMapper.map(payment, PaymentDTO.class);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    public void confirmPayment(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);

        if (payment.isEmpty()) {
            throw new EntityNotFoundException();
        }
        var paymentFound = payment.get();
        paymentFound.setStatus(Status.CONFIRMED);
        paymentRepository.save(paymentFound);
        orderClient.updatePayment(paymentFound.getOrderId());
    }

    public void updateStatus(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);

        if (payment.isEmpty()) {
            throw new EntityNotFoundException();
        }
        var paymentFound = payment.get();
        paymentFound.setStatus(Status.CONFIRMED_WITHOUT_INTEGRATION);
        paymentRepository.save(paymentFound);
    }
}
