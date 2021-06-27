package com.skcc.product.event.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductPayload {
	
	private long id;
	private String name;
	private String CategoryName;
	private String active;
	private long originalPrice;
	private long salePercentage;
	private long salePrice;
	private long resultPrice;
	private long amount;
	private String img;	
}
