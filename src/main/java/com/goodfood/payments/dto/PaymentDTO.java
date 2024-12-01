package com.goodfood.payments.dto;

import com.goodfood.payments.model.OrderItem;
import com.goodfood.payments.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PaymentDTO {
    private Long id;
    private BigDecimal value;
    private String name;
    private String cardNumber;
    private String expiration;
    private String code;
    private Status status;
    private Long orderId;
    private Long paymentMethodId;
    private List<OrderItem> itens;
}
