package com.goodfood.payments.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Order {
    private List<OrderItem> itens;
}
