package com.skcc.cart.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.skcc.cart.config.CartProductConverter;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Long accountId;

	@Column
	private Long productId;

	@Column(length = 255)
	private String productActive;

	@Column
	private Long productQuantity;
	
	@Column(columnDefinition = "TEXT")
	@Convert(converter = CartProductConverter.class)
	private CartProduct productInfo;

	@Column
	@CreationTimestamp
	private LocalDateTime createdAt;

}
