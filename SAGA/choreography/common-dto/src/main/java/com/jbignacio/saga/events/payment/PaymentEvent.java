package com.jbignacio.saga.events.payment;

import java.util.Date;
import java.util.UUID;

import com.jbignacio.saga.dto.PaymentDto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
public class PaymentEvent {

	 private final UUID eventId = UUID.randomUUID();
	 private final Date date = new Date();
	 private PaymentDto payment;
	 private PaymentStatus paymentStatus;

	 public PaymentEvent(PaymentDto payment, PaymentStatus paymentStatus) {
		 this.payment = payment;
		 this.paymentStatus = paymentStatus;
	 }

}
