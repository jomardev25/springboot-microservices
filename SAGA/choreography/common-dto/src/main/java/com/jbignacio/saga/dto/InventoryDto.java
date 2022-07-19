package com.jbignacio.saga.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
@Builder
public class InventoryDto {

	private int orderId;
	private int productId;
	private int qty;

}
