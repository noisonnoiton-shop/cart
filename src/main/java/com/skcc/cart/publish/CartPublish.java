package com.skcc.cart.publish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.skcc.cart.event.channel.CartOutputChannel;
import com.skcc.cart.event.message.CartEvent;

@Component
@EnableBinding(CartOutputChannel.class)
public class CartPublish {
	
	private CartOutputChannel cartOutputChannel;
	
	@Value("${domain.name}")
	private String domain;
	
	private static final Logger log = LoggerFactory.getLogger(CartPublish.class);

	@Autowired
	public CartPublish(CartOutputChannel cartOutputChannel) {
		this.cartOutputChannel = cartOutputChannel;
	}

	public boolean send(CartEvent cartEvent) {
		return this.cartOutputChannel.getMessageChannel().send(MessageBuilder.withPayload(cartEvent).setHeader("routeTo", domain + "." + cartEvent.getEventType()).build());
	}
}
