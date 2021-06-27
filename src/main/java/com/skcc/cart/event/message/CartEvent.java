package com.skcc.cart.event.message;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.skcc.cart.config.CartPayloadConverter;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CartEvent {

	@Id
	@SequenceGenerator( name = "event_seq_gen", sequenceName = "event_seq", allocationSize = 1 )

	@GeneratedValue(generator="event_seq_gen")
	private Long id;

	@Column(length = 255)
	private String domain;

	@Column
	private long cartId;

	@Column
	@Enumerated(EnumType.STRING)
	private CartEventType eventType;

	@Column(columnDefinition = "TEXT")
	@Convert(converter = CartPayloadConverter.class)
	private CartPayload payload;

	@Column(length = 255)
	private String txId;

	@Column
	@CreationTimestamp
	private LocalDateTime createdAt;
}
