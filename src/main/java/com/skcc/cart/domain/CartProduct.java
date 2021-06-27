package com.skcc.cart.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartProduct{
	
	private long id;
	private String name;
	private String categoryName;
	private long amount;
	private long originalPrice;
	private long salePercentage;
	private long salePrice;
	private long resultPrice;
	private String img;
	
}
