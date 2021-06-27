package com.skcc.cart.config;

import java.io.IOException;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skcc.cart.event.message.CartPayload;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CartPayloadConverter implements AttributeConverter<CartPayload, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(CartPayload tData) {

    try {
      return objectMapper.writeValueAsString(tData);
    } catch (JsonProcessingException e) {
      log.error("fail to serialize as object into Json : {}", tData, e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public CartPayload convertToEntityAttribute(String jsonStr) {

    try {
      return objectMapper.readValue(jsonStr, CartPayload.class);
    } catch (IOException e) {
      log.error("fail to deserialize as Json into Object : {}", jsonStr, e);
      throw new RuntimeException(e);
    }
  }
}
