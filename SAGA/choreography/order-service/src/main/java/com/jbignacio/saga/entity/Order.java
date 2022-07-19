package com.jbignacio.saga.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.jbignacio.saga.events.inventory.InventoryStatus;
import com.jbignacio.saga.events.order.OrderStatus;
import com.jbignacio.saga.events.payment.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
@Builder
@Table(name = "orders")
public class Order {

	@Id
	@Column(value = "id")
	private Integer orderId;

	@Column(value = "user_id")
	private Integer userId;

	@Column(value = "product_id")
	private Integer productId;

	@Column(value = "qty")
	private int qty;

	@Column(value = "price")
	private double price;

	@Column(value = "order_status")
	private OrderStatus orderStatus;

	@Column(value = "payment_status")
	private PaymentStatus paymentStatus;

	@Column(value = "inventory_status")
	private InventoryStatus inventoryStatus;

}
