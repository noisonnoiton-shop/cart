package com.skcc.cart.repository;

import java.util.List;

import com.skcc.cart.domain.Cart;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long>{
  public Cart findById(long id);
  public void deleteById(long id);
  public List<Cart> findCartByAccountId(long accountId);
  public List<Cart> findCartByAccountIdAndProductId(long accountId, long productId);
  public List<Cart> findCartByProductIdAndProductActive(long productId, String productActive);
}

