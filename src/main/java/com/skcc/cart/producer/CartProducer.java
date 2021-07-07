package com.skcc.cart.producer;

import com.skcc.cart.event.message.CartEvent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CartProducer {

  private final StreamBridge streamBridge;

  @Value("${domain.name}")
  private String domain;

  public boolean send(CartEvent cartEvent) {
    log.info("routeTo" + domain + "." + cartEvent.getEventType());

    return this.streamBridge.send("cartOutput", MessageBuilder.withPayload(cartEvent)
    .setHeader("routeTo", domain + "." + cartEvent.getEventType()).build());
  }
}