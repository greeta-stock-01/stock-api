package net.greeta.stock.order.core.events;

import net.greeta.stock.order.core.model.OrderStatus;

import lombok.Value;

@Value
public class OrderApprovedEvent {

	private final String orderId;
	private final OrderStatus orderStatus = OrderStatus.APPROVED;
	
}
