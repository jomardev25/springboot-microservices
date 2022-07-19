package com.jbignacio.saga.events.order;

import java.util.Date;
import java.util.UUID;

import com.jbignacio.saga.events.Event;
import com.jbignacio.saga.dto.OrderDto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
public class OrderEvent implements Event {

	private final UUID eventId = UUID.randomUUID();
    private final Date date = new Date();
    private OrderDto order;
    private OrderStatus orderStatus;

    public OrderEvent(OrderDto order, OrderStatus orderStatus) {
		this.order = order;
		this.orderStatus = orderStatus;
	}

	public UUID getEventId() {
		return eventId;
	}

	public Date getDate() {
		return date;
	}

}
