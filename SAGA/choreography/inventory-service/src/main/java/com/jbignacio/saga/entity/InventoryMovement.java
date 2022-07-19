package com.jbignacio.saga.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
@Builder
@Table(name = "inventory_movement")
public class InventoryMovement {

	@Id
	@Column(value = "order_id")
	private int orderId;

	@Column(value = "product_id")
	private int productId;

	@Column(value = "qty")
	private int qty;

}
