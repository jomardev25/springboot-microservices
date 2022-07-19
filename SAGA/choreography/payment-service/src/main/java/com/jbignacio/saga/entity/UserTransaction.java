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
@Builder
@Data
@Table(name = "user_transactions")
public class UserTransaction {

	@Id
	@Column(value = "order_id")
    private int orderId;

	@Column(value = "user_id")
    private int userId;

	@Column(value = "amount")
    private double amount;

}
