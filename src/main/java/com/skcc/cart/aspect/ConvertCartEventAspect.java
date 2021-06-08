package com.skcc.cart.aspect;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.skcc.cart.domain.Cart;
import com.skcc.cart.event.message.CartEventType;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class ConvertCartEventAspect {
		
	private static final Logger log = LoggerFactory.getLogger(ConvertCartEventAspect.class);

	@Pointcut("execution(* com.skcc.*.service.*.convertCartToCartEvent(..))")
	public void convertCartToCartEvent() {}
	
	@Around("convertCartToCartEvent() && args(txId, cart, cartEventType)")
	public Object setTxId(ProceedingJoinPoint pjp, String txId, Cart cart, CartEventType cartEventType) throws Throwable {
		//request에 의한 호출시 txId == null
		//subsribe에 의한 호출시 txId != null
		if(txId == null) {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

			// zuul prefilter 제거하여 수동 생성
			// txId = attr.getRequest().getHeader("X-TXID");
			UUID uuid = UUID.randomUUID();
			txId = String.format("%s-%s", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), uuid.toString());
		}
		
		return pjp.proceed(new Object[] {txId, cart, cartEventType});
	}
	
}
