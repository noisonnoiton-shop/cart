package com.skcc.cart.event.message;

import com.skcc.cart.domain.CartProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartPayload {
	
	private long id;
	private long accountId;
	private long productId;
	private String productActive;
	private long productQuantity; 
	private CartProduct productInfo;	
}
