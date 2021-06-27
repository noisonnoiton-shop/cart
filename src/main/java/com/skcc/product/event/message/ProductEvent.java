package com.skcc.product.event.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductEvent {
	
	private long id;
	private String domain;
	private long productId;
	private ProductEventType eventType;
	private ProductPayload payload;
	private String txId;
	private String createdAt;
		
}
