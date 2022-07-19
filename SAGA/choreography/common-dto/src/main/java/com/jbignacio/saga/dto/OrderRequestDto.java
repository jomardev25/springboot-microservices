package com.jbignacio.saga.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
@Builder
public class OrderRequestDto {

    private int userId;
    private int productId;
    private int orderId;
    private int orderQty;
    private double price;

}