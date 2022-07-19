package com.jbignacio.saga.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Table(name = "product_inventory")
public class ProductInventory {

	@Id
	@Column(value="product_id")
	private int productId;
	private int qty;

}
