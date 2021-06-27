package com.skcc.cart.service;

import java.time.LocalDateTime;
import java.util.List;

import com.skcc.cart.domain.Cart;
import com.skcc.cart.domain.CartProduct;
import com.skcc.cart.event.message.CartEvent;
import com.skcc.cart.event.message.CartEventType;
import com.skcc.cart.event.message.CartPayload;
import com.skcc.cart.publish.CartPublish;
import com.skcc.cart.repository.CartEventRepository;
import com.skcc.cart.repository.CartRepository;
import com.skcc.product.event.message.ProductEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {
	
	// @Autowired
	// private CartMapper cartMapper;

	private CartRepository cartRepository;
	private CartEventRepository cartEventRepository;
	private CartPublish cartPublish;
	
	@Value("${domain.name}")
	private String domain;
	
	
	@Autowired
	private CartService cartService;
	
	private static final Logger log = LoggerFactory.getLogger(CartService.class);

	@Autowired
	public CartService(CartRepository cartRepository, CartEventRepository cartEventRepository, CartPublish cartPublish) {
		this.cartRepository = cartRepository;
		this.cartEventRepository = cartEventRepository;
		this.cartPublish = cartPublish;
	}
	
	public List<Cart> findCartByAccountId(long accountId) {
		// return this.cartMapper.findCartByAccountId(accountId);
		return this.cartRepository.findCartByAccountId(accountId);
	}
	
	public List<CartEvent> getCartEvent() {
		// return this.cartMapper.getCartEvent();
		return this.cartEventRepository.findAll();
	}
	
	public boolean addCartAndCreatePublishEvent(Cart cart) {
		boolean result = false;
		
		try {
			this.cartService.AddCartAndCreatePublishCartProductAddedEvent(cart);
			result = true;
		} catch(Exception e) {
			try {
				result = false;
				e.printStackTrace();
				this.cartService.createPublishCartProductAddFailedEvent(cart);
			}catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return result;
	}
	
	public boolean deleteCartAndCreatePublishEvent(long id) {
		boolean result = false;
		
		try {
			this.cartService.deleteCartAndCreatePublishCartProductDeletedEvent(id);
			result = true;
		} catch(Exception e) {
			try {
				result = false;
				e.printStackTrace();
				this.cartService.createPublishCartProductDeleteFailedEvent(id);
			}catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return result;
	}
	
	public boolean setCartProductInactiveAndProductInfoAndCreatePublishEvent(ProductEvent productEvent) {
		boolean result = false;
		List<Cart> carts = this.findCartToBeProductInactiveById(productEvent);
		try {
			this.cartService.setCartProductInactiveAndProductInfoAndCreatePublishCartProductInactiveEvent(productEvent, carts);
			result = true;
		} catch(Exception e) {
			try {
				result = false;
				e.printStackTrace();
				this.cartService.createPublishCartProductInactiveFailedEvent(productEvent, carts);
			}catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return result;
	}
	
	public boolean setCartProductActiveAndProductInfoAndCreatePublishEvent(ProductEvent productEvent) {
		boolean result = false;
		List<Cart> carts = this.findCartToBeProductActiveById(productEvent);
		try {
			this.cartService.setCartProductActiveAndProductInfoAndCreatePublishCartProductActiveEvent(productEvent, carts);
			result = true;
		} catch(Exception e) {
			try {
				result = false;
				e.printStackTrace();
				this.cartService.createPublishCartProductActiveFailedEvent(productEvent, carts);
			}catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return result;
	}
	
	public boolean setCartQuantityAndCreatePublishEvent(long id, long quantity) {
		boolean result = false;
		Cart cart = this.findById(id);
		cart.setProductQuantity(quantity);
		try {
			this.cartService.setCartQuantityAndCreatePublishCartQuantityEditedEvent(cart);
			result = true;
		} catch(Exception e) {
			try {
				result = false;
				e.printStackTrace();
				this.cartService.CreatePublishCartQuantityEditedEvent(cart);
			}catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Transactional
	public void AddCartAndCreatePublishCartProductAddedEvent(Cart cart) throws Exception{
		Cart resultCart = this.addCart(cart);
		this.createPublishCartEvent(null, resultCart, CartEventType.CartProductAdded);
	}
	
	@Transactional
	public void createPublishCartProductAddFailedEvent(Cart cart) throws Exception{
		this.createPublishCartEvent(null, cart, CartEventType.CartProductAddFailed);
	}
	
	@Transactional
	public void deleteCartAndCreatePublishCartProductDeletedEvent(long id) throws Exception{
		Cart resultCart = this.findById(id);
		this.deleteCart(id);
		this.createPublishCartEvent(null, resultCart, CartEventType.CartProductDeleted);
	}
	
	@Transactional
	public void createPublishCartProductDeleteFailedEvent(long id) throws Exception{
		Cart resultCart = this.findById(id);
		this.createPublishCartEvent(null, resultCart, CartEventType.CartProductDeleteFailed);
	}
	
	@Transactional
	public void setCartProductInactiveAndProductInfoAndCreatePublishCartProductInactiveEvent(ProductEvent productEvent, List<Cart> carts) {
		Cart productEventCart = this.convertProductEventToCart(productEvent);
		this.setCartProductInactiveAndProductInfo(productEventCart);
		for(Cart cart : carts) {
			this.createPublishCartEvent(productEvent.getTxId(), this.convertProductEventCartToCart(cart, productEventCart), CartEventType.CartProductInactive);
		}
	}
	
	@Transactional
	public void createPublishCartProductInactiveFailedEvent(ProductEvent productEvent, List<Cart> carts) {
		Cart productEventCart = this.convertProductEventToCart(productEvent);
		for(Cart cart : carts) {
			this.createPublishCartEvent(null, this.convertProductEventCartToCart(cart, productEventCart), CartEventType.CartProductInactiveFailed);
		}
	}
	
	@Transactional
	public void setCartProductActiveAndProductInfoAndCreatePublishCartProductActiveEvent(ProductEvent productEvent, List<Cart> carts) {
		Cart productEventCart = this.convertProductEventToCart(productEvent);
		this.setCartProductActiveAndProductInfo(productEventCart);
		for(Cart cart : carts) {
			this.createPublishCartEvent(productEvent.getTxId(), this.convertProductEventCartToCart(cart, productEventCart), CartEventType.CartProductActive);
		}
	}
	
	@Transactional
	public void createPublishCartProductActiveFailedEvent(ProductEvent productEvent, List<Cart> carts) throws Exception{
		Cart productEventCart = this.convertProductEventToCart(productEvent);
		for(Cart cart : carts) {
			this.createPublishCartEvent(null, this.convertProductEventCartToCart(cart, productEventCart), CartEventType.CartProductActiveFailed);
		}
	}
	
	@Transactional
	public void setCartQuantityAndCreatePublishCartQuantityEditedEvent(Cart cart) throws Exception {
		this.setCartQuantity(cart);
		this.createPublishCartEvent(null, cart, CartEventType.CartQuantityEdited);
	}
	
	@Transactional
	public void CreatePublishCartQuantityEditedEvent(Cart cart) throws Exception{
		this.createPublishCartEvent(null, cart, CartEventType.CartQuantityEditFailed);
	}
	
	public Cart addCart(Cart cart) throws Exception {
		if(!this.addCartValidationCheck(cart))
			throw new Exception();
		// this.cartMapper.addCart(cart);
		this.cartRepository.save(cart);
		
		return cart;
	}

	public void deleteCart(long id) {
		// return this.cartMapper.deleteCart(id);
		this.cartRepository.deleteById(id);
	}

	public void createPublishCartEvent(String txId, Cart cart, CartEventType cartEventType) {
		CartEvent cartEvent = this.cartService.convertCartToCartEvent(txId, cart, cartEventType);
		this.createCartEvent(cartEvent);
		this.publishCartEvent(cartEvent);
	}
	
	public void createCartEvent(CartEvent cartEvent) {
		// this.cartMapper.createCartEvent(cartEvent);
		this.cartEventRepository.save(cartEvent);
	}
	 
	public void publishCartEvent(CartEvent cartEvent) {
		this.cartPublish.send(cartEvent);
	}
	
	public Cart findById(long id) {
		// return this.cartMapper.findById(id);
		return this.cartRepository.findById(id);
	}
	
	public void setCartProductInactiveAndProductInfo(Cart cart) {
		// this.cartMapper.setCartProductInactiveAndProductInfo(cart);
		cart.setProductActive("active");
		this.cartRepository.save(cart);
	}
	
	public void setCartProductActiveAndProductInfo(Cart cart) {
		// this.cartMapper.setCartProductActiveAndProductInfo(cart);
		cart.setProductActive("inactive");
		this.cartRepository.save(cart);
	}
	
	public void setCartQuantityValidationCheck(Cart cart) throws Exception{
		if(cart.getProductQuantity() < 0)
			throw new Exception();
	}
	
	public void setCartQuantity(Cart cart) throws Exception {
		this.setCartQuantityValidationCheck(cart);
		// this.cartMapper.setCartQuantity(cart.getId(), cart.getProductQuantity());
		this.cartRepository.save(cart);
	}
	
	public List<Cart> findCartToBeProductInactiveById(ProductEvent productEvent){
		// return this.cartMapper.findCartToBeProductInactiveById(productEvent.getProductId());
		return this.cartRepository.findCartByProductIdAndProductActive(productEvent.getProductId(), "active");
	}
	
	public List<Cart> findCartToBeProductActiveById(ProductEvent productEvent){
		// return this.cartMapper.findCartToBeProductActiveById(productEvent.getProductId());
		return this.cartRepository.findCartByProductIdAndProductActive(productEvent.getProductId(), "inactive");
	}
	
	public Cart convertProductEventCartToCart(Cart originCart, Cart convertedCart) {
		originCart.setProductInfo(convertedCart.getProductInfo());
		originCart.setProductActive(convertedCart.getProductActive());
		
		return originCart;
	}
	
	public Cart convertProductEventToCart(ProductEvent productEvent) {
		CartProduct cartProduct = new CartProduct();
		cartProduct.setId(productEvent.getPayload().getId());
		cartProduct.setName(productEvent.getPayload().getName());
		cartProduct.setCategoryName(productEvent.getPayload().getCategoryName());
		cartProduct.setOriginalPrice(productEvent.getPayload().getOriginalPrice());
		cartProduct.setSalePercentage(productEvent.getPayload().getSalePercentage());
		cartProduct.setSalePrice(productEvent.getPayload().getSalePrice());
		cartProduct.setResultPrice(productEvent.getPayload().getResultPrice());
		cartProduct.setAmount(productEvent.getPayload().getAmount());
		
		Cart cart = new Cart();
		cart.setProductId(productEvent.getProductId());
		cart.setProductInfo(cartProduct);
		cart.setProductActive(productEvent.getPayload().getActive());
		
		return cart;
	}
	
	public CartEvent convertCartToCartEvent(String txId, Cart cart, CartEventType cartEventType) {
		log.info("in service txId : {}", txId);
		
		//delete 를 한 경우 조회해도 데이터가 없다. 이 때 이벤트 로그는 요청된 cart 정보로 처리한다.
		Cart resultCart = this.findById(cart.getId());
		if(resultCart != null)
			cart = resultCart;
		
		CartEvent cartEvent = new CartEvent();
		// cartEvent.setId(this.cartMapper.getCartEventId());
		cartEvent.setCartId(cart.getId());
		cartEvent.setDomain(domain);
		cartEvent.setEventType(cartEventType);
		cartEvent.setPayload(new CartPayload(cart.getId(), cart.getAccountId(), cart.getProductId(), cart.getProductActive(), cart.getProductQuantity(), cart.getProductInfo()));
		cartEvent.setTxId(txId);
		cartEvent.setCreatedAt(LocalDateTime.now());
		
		log.info("in service cartEvent : {}", cartEvent.toString());
		return cartEvent;
	}
	
	public boolean addCartValidationCheck(Cart cart) {
		boolean valid = true;
		// if(this.cartMapper.findCartByProductId(cart.getAccountId(), cart.getProductId()) > 0)
		if(this.cartRepository.findCartByAccountIdAndProductId(cart.getAccountId(), cart.getProductId()).size() > 0)
			valid = false;
		return valid;
	}
}
