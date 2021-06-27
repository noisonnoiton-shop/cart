package com.skcc.cart.repository;

import com.skcc.cart.event.message.CartEvent;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartEventRepository extends JpaRepository<CartEvent, Long>{
}

