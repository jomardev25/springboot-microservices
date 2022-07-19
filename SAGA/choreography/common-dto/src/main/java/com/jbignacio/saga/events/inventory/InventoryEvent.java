package com.jbignacio.saga.events.inventory;

import java.util.Date;
import java.util.UUID;

import com.jbignacio.saga.dto.InventoryDto;
import com.jbignacio.saga.events.Event;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
public class InventoryEvent implements Event {

	private final UUID eventId = UUID.randomUUID();
    private final Date date = new Date();
    private InventoryDto inventory;
    private InventoryStatus inventoryStatus;

    public InventoryEvent(InventoryDto inventory, InventoryStatus inventoryStatus) {
        this.inventory = inventory;
        this.inventoryStatus = inventoryStatus;
    }

	@Override
	public UUID getEventId() {
		return eventId;
	}

	@Override
	public Date getDate() {
		return date;
	}

}
