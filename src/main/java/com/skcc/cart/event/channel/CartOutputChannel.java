package com.skcc.cart.event.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CartOutputChannel {
	String CartOutput = "CartOutput";
	
	@Output(CartOutputChannel.CartOutput)
	MessageChannel getMessageChannel();
}
