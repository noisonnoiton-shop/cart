package com.skcc.cart.subscribe;

import com.skcc.cart.event.channel.CartInputChannel;
import com.skcc.cart.service.CartService;
import com.skcc.product.event.message.ProductEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

// @Component
// @EnableBinding(CartInputChannel.class)
public class CartSubscribe {

	private CartService cartService;
	
	@Autowired
	public CartSubscribe(CartService cartService) {
		this.cartService = cartService;
	}
	
	// @StreamListener(CartInputChannel.productSoldOut)
	public void setProductActiveToInactive(ProductEvent productEvent) {
		this.cartService.setCartProductInactiveAndProductInfoAndCreatePublishEvent(productEvent);
	}
	
	// @StreamListener(CartInputChannel.productAmountAdded)
	public void setProductAmountAdded(ProductEvent productEvent) {
		this.cartService.setCartProductActiveAndProductInfoAndCreatePublishEvent(productEvent);
	}
	
}
