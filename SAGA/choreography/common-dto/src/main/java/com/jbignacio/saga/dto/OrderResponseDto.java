package com.jbignacio.saga.dto;

import com.jbignacio.saga.events.order.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
@Builder
public class OrderResponseDto {

    private Integer orderId;
    private Integer userId;
    private Integer productId;
    private Integer qty;
    private Double price;
    private OrderStatus status;

}