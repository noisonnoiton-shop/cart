package com.skcc.cart.event.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface CartInputChannel {
	String productSoldOut = "productSoldOut";
	String productAmountAdded = "productAmountAdded";
	
	@Input(CartInputChannel.productSoldOut)
	SubscribableChannel productSoldOut();
	
	@Input(CartInputChannel.productAmountAdded)
	SubscribableChannel productAmountAdded();
}
